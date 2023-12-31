package kingdomBuilder.network.internal;

import kingdomBuilder.network.generated.ProtocolConsumer;
import kingdomBuilder.network.generated.ProtocolDeserializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles IO events reported by {@link ClientSelectorImpl} and acts as "glue" between
 * {@link kingdomBuilder.network.Client} and {@link ClientSelectorImpl}.
 */
public class IOHandler {
    private static final String COMMAND_TERMINATOR = "\n";

    private final Selector selector;
    private final SocketChannel channel;
    private final Queue<ByteBuffer> writeQueue;
    private final AtomicBoolean connected;
    private ByteBuffer buffer;
    private String stringBuffer;
    private String eol;
    private ProtocolConsumer consumer;

    /**
     * Initializes the handler to default state.
     * @param selector the selector to which the channel is registered to.
     * @param channel the channel to operate upon.
     */
    public IOHandler(Selector selector, SocketChannel channel) {
        this.selector = selector;
        this.channel = channel;
        this.writeQueue = new ConcurrentLinkedQueue<>();
        this.connected = new AtomicBoolean(false);
        this.buffer = null;
        this.stringBuffer = "";
        this.eol = "";
        this.consumer = null;
    }

    /**
     * Sets the consumer, which is notified about incoming packets.
     * @param consumer the consumer to notify.
     */
    public void setConsumer(ProtocolConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Used by {@link kingdomBuilder.network.ClientSelector}, when the connection has been established
     * or errored out.
     * @param key the key under which the underlying socket was registered.
     */
    public void onIsConnectable(SelectionKey key) {
        try {
            if (channel.finishConnect()) {
                connected.set(true);

                key.interestOps(SelectionKey.OP_READ);
                selector.wakeup();

                int bufferSize = channel.socket().getReceiveBufferSize();
                buffer = ByteBuffer.allocate(bufferSize);

                tryFlush();
            }
        } catch(IOException exc) {
            connected.set(false);
            /* TODO: Notify consumer, that connection was lost. */
        }
    }

    /**
     * Used by {@link kingdomBuilder.network.ClientSelector}, when the connection received data.
     * @param key the key under which the underlying socket was registered.
     */
    public synchronized void onIsReadable(SelectionKey key) {
        int bytesRead = 0;
        int totalBytesRead = 0;
        try {
            do {
                bytesRead = channel.read(buffer);
                totalBytesRead += bytesRead;
            }
            while(bytesRead > 0 && buffer.hasRemaining());
        } catch(IOException ignored) {}

        // The selector reports 'readable' despite no data being received
        // on linux. Is this a bug in JRE?
        if(totalBytesRead <= 0) return;

        String contents = new String(buffer.array(), 0, totalBytesRead); // .trim();
        buffer.clear();
        buffer.rewind();


        stringBuffer = stringBuffer + contents;

        if(eol.isEmpty()) {
            if(stringBuffer.contains("\r\n"))
                eol = "\r\n";
            else if(stringBuffer.contains("\n"))
                eol = "\n";
            // Haven't received a full command yet; returning till later.
            else
                return;
        }

        // Splits the string buffer into chunks like these:
        // "COMMAND(eol)", "COMMAND2(eol)", "COMMAND3(eol)"
        // the last split may not be a completely transmitted command yet,
        // thus the delimiter is retained to check that each command is complete.
        var commands = List.of(stringBuffer.split("(?<=\n)"));
        commands
            .forEach(c -> {
                if(c.endsWith(eol))
                    ProtocolDeserializer.deserialize(c.trim(), consumer);
            });

        String lastCommand = !commands.isEmpty()
            ? commands.get(commands.size() - 1)
            : "";

        stringBuffer = !lastCommand.endsWith(eol)
                ? lastCommand
                : "";

        /*
        Scanner t = new Scanner(contents);
        while(t.hasNextLine()) {
            String line = t.nextLine();
            if(consumer != null)
                ProtocolDeserializer.deserialize(line, consumer);
        }
        */
    }

    /**
     * Used by {@link kingdomBuilder.network.ClientSelector}, when the connection is ready to
     * transmit more data.
     * @param key the key under which the underlying socket was registered.
     */
    public void onIsWriteable(SelectionKey key) {
        try { tryFlush(); }
        catch(IOException exc) {
            connected.set(false);
            /* TODO: Notify consumer, that connection was lost. */
        }
    }

    /**
     * Pushes the command to the internal command queue and tries to flush the queue.
     * @param command the command to transmit.
     * @throws IOException Thrown when the connection was lost.
     */
    public void sendCommand(String command) throws IOException {
        final String commandLine = command + COMMAND_TERMINATOR;
        final ByteBuffer buffer = ByteBuffer.wrap(commandLine.getBytes());

        writeQueue.offer(buffer);
        try {
            tryFlush();
        } catch(Exception e) {
            connected.set(false);
            throw e;
        }
    }

    /**
     * {@return Returns whether any commands are still pending to be written.}
     */
    public boolean hasPendingCommands() {
        return !writeQueue.isEmpty();
    }

    /**
     * {@return Returns whether the channel/socket is connected.}
     */
    public boolean isConnected() {
        return connected.get();
    }

    /**
     * Disconnects the channel (i.e. closes the socket) and removes it from the selector.
     */
    public void disconnect() {
        try {
            SelectionKey key = channel.keyFor(selector);
            key.cancel();
            channel.close();
        } catch(Exception unused) {}
    }

    /**
     * Writes all commands to the socket either until all commands are sent
     * or when the sockets internal buffer is full.
     *
     * Registers this socket for write selection, if the buffer was exhausted
     * before all commands were transmitted and socket is not registered for this
     * intent yet.
     */
    private void tryFlush() throws IOException {
        SelectionKey key = channel.keyFor(selector);
        if(key == null) return;

        if(!connected.get()) {
            key.interestOpsOr(SelectionKey.OP_WRITE);
            selector.wakeup();
            return;
        }

        // Queue needs to be synchronized, because buffer is only popped,
        // once it's been transmitted completely.
        synchronized (writeQueue) {
            while (!writeQueue.isEmpty()) {
                ByteBuffer buffer = writeQueue.peek();

                int bytesWritten = 0;
                do bytesWritten = channel.write(buffer);
                while (bytesWritten > 0 && buffer.hasRemaining());

                // Successfully transmitted the message;
                // remove the current and start working on the next.
                if (!buffer.hasRemaining()) writeQueue.poll();
                else {

                    // Internal buffer is full; register to be notified,
                    // when it is writeable again.
                    key.interestOpsOr(SelectionKey.OP_WRITE);
                    selector.wakeup();
                    return;
                }
            }
        }

        // All stored commands have been written;
        // we are not interested in writing until
        // sendCommand is being called again.
        key.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

}
