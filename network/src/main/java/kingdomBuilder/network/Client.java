package kingdomBuilder.network;

import kingdomBuilder.network.protocol.ClientJoined;
import kingdomBuilder.network.protocol.ClientLeft;
import kingdomBuilder.network.protocol.Message;

import java.util.List;

/**
 * Represents a client, which interacts with the server it is connected to.
 */
public abstract class Client {
    protected String name;
    protected int clientId;
    protected int gameId;

    public final Event<Client> onConnected;
    public final Event<Client> onDisconnected;

    public final Event<Client> onLoggedIn;
    public final Event<Client> onKicked;
    public final Event<ClientJoined> onClientJoined;
    public final Event<ClientLeft> onClientLeft;
    public final Event<Message> onMessageReceived;

    public Client() {
        onConnected = new Event<>();
        onDisconnected = new Event<>();
        onLoggedIn = new Event<>();
        onKicked = new Event<>();
        onClientJoined = new Event<>();
        onClientLeft = new Event<>();
        onMessageReceived = new Event<>();
    }

    /**
     * Logs into the server with a preferred name.
     * @param preferredName The preferred name.
     *
     * @implSpec This method may be implemented non-blocking.
     */
    public abstract void login(String preferredName);

    /**
     * Logs out from the server.
     *
     * @implSpec This method may be implemented non-blocking.
     */
    public abstract void logout();

    /**
     * Sends a message to all recipients.
     * @param recipients The recipients of the message.
     * @param message The message to send.
     */
    public abstract void chat(List<Integer> recipients, String message);

    /**
     * {@return Returns whether the client is connected.}
     */
    public abstract boolean isConnected();

    /**
     * Closes the client's connection.
     */
    public abstract void disconnect();

}
