package kingdomBuilder.network;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Manages and notifies instance of {@link Client}, when e.g. some data
 * was received.
 * <br><br>
 * This class implements {@link Runnable} and is supposed to be run
 * in its separate thread.
 * <br><br>
 * The thread can be stopped gracefully by calling {@link ClientSelector#stop()}
 * and then {@link Thread#interrupt()}.
 */
public interface ClientSelector extends Runnable {

    /**
     * Creates a client with a new connection to given address.
     * @param address the address to connect to.
     * @return The new client.
     * @throws IOException Thrown when no connection could be initiated.
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
