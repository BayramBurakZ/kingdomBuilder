package kingdomBuilder;

import kingdomBuilder.gui.controller.MainViewController;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.model.GameDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.internal.ClientSelectorImpl;
import kingdomBuilder.annotations.State;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of the Kingdom Builder application.
 */
@State
public class KBState {
    // public for testing
    // TODO? :probably just make these ObservableLists directly

    /**
     * Represents the MainViewController.
     */
    public MainViewController controller;

    /**
     * Maps client id to the client info of connected clients.
     */
    public final Map<Integer, ClientDAO> clients;

    /**
     * Maps game id to the game info of created games.
     */
    public final Map<Integer, GameDAO> games;

    /**
     * Stores the selector, which handles network IO.
     */
    public final ClientSelector selector;

    /**
     * Stores the thread, which runs the selector.
     */
    public Thread selectorThread;

    /**
     * Represents the main client.
     */
    public Client client;

    /**
     * Represents the login name entered by the player.
     */
    public String clientPreferredName;

    /**
     * Whether the client is currently connecting or not.
     */
    public boolean isConnecting;

    /**
     * Shows whether the client is connected to the server.
     */
    public boolean isConnected;

    /**
     * Shows whether the connection to the server failed.
     */
    public boolean failedToConnect;

    public KBState(MainViewController controller, Map<Integer, ClientDAO> clients, Map<Integer, GameDAO> games, ClientSelector selector, Thread selectorThread, Client client, String clientPreferredName, boolean isConnecting, boolean isConnected, boolean failedToConnect) {
        this.controller = controller;
        this.clients = clients;
        this.games = games;
        this.selector = selector;
        this.selectorThread = selectorThread;
        this.client = client;
        this.clientPreferredName = clientPreferredName;
        this.isConnecting = isConnecting;
        this.isConnected = isConnected;
        this.failedToConnect = failedToConnect;
    }

    /**
     * Initializes the state with initial value.
     */
    public KBState() throws IOException {
        clients = new HashMap<>();
        games = new HashMap<>();
        selector = new ClientSelectorImpl();
        isConnecting = false;
        isConnected = false;
        failedToConnect = false;
    }

    /**
     * Creates the same KBState object as the given KBState object.
     * @param other the received KBState
     */
    public KBState(KBState other) {
        controller = other.controller;
        clients = other.clients;
        games = other.games;
        selector = other.selector;
        selectorThread = other.selectorThread;
        client = other.client;
        clientPreferredName = other.clientPreferredName;
        isConnecting = other.isConnecting;
        isConnected = other.isConnected;
        failedToConnect = other.failedToConnect;
    }

}
