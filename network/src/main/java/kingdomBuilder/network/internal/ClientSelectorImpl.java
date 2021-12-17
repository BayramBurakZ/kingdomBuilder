package kingdomBuilder.network.internal;

import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;


public class ClientSelectorImpl implements ClientSelector {
    private final Selector selector;
    private final AtomicBoolean running;
    private final AtomicBoolean stopped;

    public ClientSelectorImpl() throws IOException {
        this.selector = Selector.open();
        this.running = new AtomicBoolean(false);
        this.stopped = new AtomicBoolean(false);
    }

    /**
     * Creates a new client, which connects to given address.
     *
     * @param address The address to connect to.
     * @return A client representing the newly created connection.
     * @throws IOException
     */
    @Override
    public Client connect(InetSocketAddress address) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(address);

        IOHandler handler = new IOHandler(selector, channel);
        channel.register(selector, SelectionKey.OP_CONNECT, handler);

        return new ClientImpl(handler);
    }

    /**
     * {@return Returns whether the listening loop is running.}
     */
    @Override
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Stops the selection/listening looping and interrupts the current thread.
     */
    @Override
    public void stop() {
        stopped.set(true);
    }

    @Override
    public void run() {
        running.set(true);

        while(!stopped.get()) {
            try { selector.select(this::onSelect); }
            catch (Exception exc) { exc.printStackTrace(); break; }
        }

        running.set(false);
        stopped.set(false);
    }

    /**
     * Callback used within run, which is invoked, whenever a socket either
     * successfully establishes a connection or receives data from the server.
     * @param key The key of the channel.
     */
    private void onSelect(SelectionKey key) {
        IOHandler handler = (IOHandler) key.attachment();
        if(key.isValid() && key.isConnectable()) handler.onIsConnectable(key);
        if(key.isValid() && key.isReadable()) handler.onIsReadable(key);
        if(key.isValid() && key.isWritable()) handler.onIsWriteable(key);
    }
}
