package kingdomBuilder;

import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.model.GameDAO;
import kingdomBuilder.network.Client;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of the Kingdom Builder application.
 */
public class KBState {
    // public for testing
    // TODO? :probably just make these ObservableLists directly

    /**
     * Represents the SceneLoader.
     */
    public SceneLoader sceneLoader;

    /**
     * Maps client id to the client info of connected clients.
     */
    public Map<Integer, ClientDAO> clients = new HashMap<>();

    /**
     * Maps game id to the game info of created games.
     */
    public Map<Integer, GameDAO> games = new HashMap<>();

    /**
     * Represents the main client.
     */
    public Client client;

    /**
     * Represents the thread of the client.
     */
    public Thread clientThread;

    /**
     * Represents the login name entered by the player.
     */
    public String clientPreferredName;

    /**
     * Shows whether the client is connected to the server.
     */
    public boolean isConnected = false;

    /**
     * Shows whether the connection to the server failed.
     */
    public boolean failedToConnect = false;

    /**
     * Represents the default constructor.
     */
    public KBState() { }

    /**
     * Creates the same KBState object as the given KBState object.
     * @param other the received KBState
     */
    public KBState(KBState other) {
        sceneLoader = other.sceneLoader;
        clients = other.clients;
        games = other.games;
        client = other.client;
        clientThread = other.clientThread;
        clientPreferredName = other.clientPreferredName;
        isConnected = other.isConnected;
        failedToConnect = other.failedToConnect;
    }
}
