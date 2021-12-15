package kingdomBuilder;

import kingdomBuilder.gui.controller.MainViewController;
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
     * Represents the MainViewController
     */
    public MainViewController controller;

    /**
     * Contains the connected clients with its id and ClientDAO.
     */
    public Map<Integer, ClientDAO> clients = new HashMap<>();

    /**
     * Contains the running games with its id and GameDAO.
     */
    public Map<Integer, GameDAO> games = new HashMap<>();

    /**
     * Represents the main client
     */
    public Client client;

    /**
     * Represents the thread of the client
     */
    public Thread clientThread;

    /**
     * Represents the login name entered by the player
     */
    public String clientPreferredName;

    /**
     * Shows whether the client is connected to the server
     */
    public boolean isConnected = false;

    /**
     * Shows whether the connection to the server failed
     */
    public boolean failedToConnect = false;

    /**
     * Represents the default constructor
     */
    public KBState() { }

    /**
     * Creates the same KBState object as the given KBState object
     * @param other the received KBState
     */
    public KBState(KBState other) {
        controller = other.controller;
        clients = other.clients;
        games = other.games;
        client = other.client;
        clientThread = other.clientThread;
        clientPreferredName = other.clientPreferredName;
        isConnected = other.isConnected;
        failedToConnect = other.failedToConnect;
    }
}
