package kingdomBuilder.network;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface ClientSelector extends Runnable {

    /**
     * Creates a client with a new connection to given address.
     * @param address The address to connect to.
     * @return The new client.
     * @throws IOException
     */
    Client connect(InetSocketAddress address) throws IOException;

    /**
     * {@return Returns whether the selection/listening loop was started.}
     */
    boolean isRunning();

    /**
     * Stops the selection/listening loop.
     */
    void stop();
}
