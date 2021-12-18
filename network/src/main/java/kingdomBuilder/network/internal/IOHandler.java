package kingdomBuilder.network.internal;

import kingdomBuilder.network.generated.ProtocolConsumer;
import kingdomBuilder.network.generated.ProtocolDeserializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IOHandler {
    private static final String COMMAND_TERMINATOR = "\n";
    private static final int RECEIVE_BUFFER_SIZE = 256;

    private final Selector selector;
    private final SocketChannel channel;
    private final Queue<ByteBuffer> writeQueue;
    private final StringBuilder lineBuilder;
    private ByteBuffer buffer;
    private ProtocolConsumer consumer;
    private boolean connected;


    public IOHandler(Selector selector, SocketChannel channel) {
        this.selector = selector;
        this.channel = channel;
        this.writeQueue = new ConcurrentLinkedQueue<>();
        this.lineBuilder = new StringBuilder();
        this.buffer = null;
        this.consumer = null;
        this.connected = false;
    }

    public ProtocolConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(ProtocolConsumer consumer) {
        this.consumer = consumer;
    }

    public synchronized void onIsConnectable(SelectionKey key) {
        try {
            if (channel.finishConnect()) {
                System.out.println("DID CONNECT!");
                connected = true;

                key.interestOps(SelectionKey.OP_READ);
                selector.wakeup();

                int bufferSize = channel.socket().getReceiveBufferSize();
                buffer = ByteBuffer.allocate(bufferSize);

                // Transmit all messages, that might have been 'send' before the connection
                // process finished.
                onIsWriteable(key);
            }
        } catch(IOException ignored) {}
    }

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

        if(totalBytesRead <= 0) {
            System.out.println("Selector woke up with no data.");
            return;
        }

        String contents = new String(buffer.array(), 0, totalBytesRead).trim();
        buffer.clear();
        buffer.rewind();

        System.out.println("Received: " + contents);

        if(consumer != null)
            ProtocolDeserializer.deserialize(contents, consumer);

    }

    public synchronized void onIsWriteable(SelectionKey key) {
        while(!writeQueue.isEmpty()) {
            final ByteBuffer buffer = writeQueue.peek();
            int bytesWritten = 0;

            try {
                do bytesWritten = channel.write(buffer);
                while(bytesWritten > 0 && buffer.hasRemaining());
            } catch(IOException ignored) {}

            if(buffer.hasRemaining()) {
                key.interestOpsOr(SelectionKey.OP_WRITE);
                return;
            }
        }

        key.interestOps(SelectionKey.OP_READ);
    }

    public synchronized void sendCommand(String command) {
        final String commandLine = command + COMMAND_TERMINATOR;
        final ByteBuffer buffer = ByteBuffer.wrap(commandLine.getBytes());
        int bytesWritten = 0;

        if(!writeQueue.isEmpty() || !connected) {
            writeQueue.offer(buffer);
            return;
        }

        try {
            do bytesWritten = channel.write(buffer);
            while(bytesWritten > 0 && buffer.hasRemaining());
        } catch(IOException ignored) {}

        if(buffer.hasRemaining()) {
            writeQueue.offer(buffer);
            SelectionKey key = channel.keyFor(selector);
            key.interestOpsOr(SelectionKey.OP_WRITE);
        }
    }

    public boolean hasPendingCommands() {
        return !writeQueue.isEmpty();
    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnect() {
        try {
            SelectionKey key = channel.keyFor(selector);
            key.cancel();
            channel.close();
        } catch(Exception exc) {}
    }

}
