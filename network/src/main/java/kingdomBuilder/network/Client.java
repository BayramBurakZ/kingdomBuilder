package kingdomBuilder.network;

import kingdomBuilder.network.protocol.ClientJoined;
import kingdomBuilder.network.protocol.ClientLeft;
import kingdomBuilder.network.protocol.Message;

import java.util.List;

/**
 * Represents a client, which interacts with the server it is connected to.
 */
public abstract class Client {
    public static final int NO_ID = -1;

    protected String name;
    protected int clientId;
    protected int gameId;

    public final Event<Client> onConnected;
    public final Event<Client> onDisconnected;

    /**
     * Event, that gets dispatched, when the client successfully logged into the server.
     */
    public final Event<Client> onLoggedIn;

    /**
     * Event, that gets dispatched, when the client was kicked from the server.
     */
    public final Event<Client> onKicked;

    /**
     * Event, that gets dispatched, whever a player joins the server.
     */
    public final Event<ClientJoined> onClientJoined;

    /**
     * Event, that gets dispatched, whenever a player leaves the server.
     */
    public final Event<ClientLeft> onClientLeft;

    /**
     * Event, that gets dispatched, whenever a (chat) message is received.
     */
    public final Event<Message> onMessageReceived;

    public Client() {
        name = null;
        clientId = -1;
        gameId = -1;

        onConnected = new Event<>();
        onDisconnected = new Event<>();
        onLoggedIn = new Event<>();
        onKicked = new Event<>();
        onClientJoined = new Event<>();
        onClientLeft = new Event<>();
        onMessageReceived = new Event<>();
    }

    /**
     * {@return Returns the name of the client or null, if the client is NOT logged in.}
     */
    public String getName() {
        return name;
    }

    /**
     * {@return Returns the id of the client or NO_ID, if the client isn't logged in yet.}
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * {@return Returns the id of the current game or NO_ID, if the client hasn't joined any games.}
     */
    public int getGameId() {
        return gameId;
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

    /**
     * {@return Returns whether the client has commands stored internally,
     *          that weren't transmitted sucessfully to the server yet.}
     */
    public abstract boolean hasPendingCommands();

}
