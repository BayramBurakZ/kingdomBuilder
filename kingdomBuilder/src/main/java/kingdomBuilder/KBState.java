package kingdomBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.internal.ClientSelectorImpl;
import kingdomBuilder.annotations.State;
import kingdomBuilder.network.protocol.GameData;

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
     * Represents the SceneLoader.
     */
    public SceneLoader sceneLoader;

    /**
     * Maps client id to the client info of connected clients.
     */
    public final Map<Integer, ClientDAO> clients;

    /**
     * Maps game id to the game info of created games.
     */
    public final Map<Integer, GameData> games;

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
    public boolean failedToConnect = false;

    /**
     * Represents if the better color mode is active.
     */
    public boolean betterColorsActiv = false;

    /**
     * Represents a List with all the available quadrants on the server;
     */
    public ObservableList<Integer> quadrantIDs = FXCollections.observableArrayList(0, 1, 2, 3, 4);

    /**
     * Represents the game.
     */
    public Game game;

    public KBState(SceneLoader sceneLoader, Map<Integer, ClientDAO> clients, Map<Integer, GameData> games, ClientSelector selector, Thread selectorThread, Client client, String clientPreferredName, boolean isConnecting, boolean isConnected, boolean failedToConnect, boolean betterColorsActiv, ObservableList<Integer> quadrantIDs, Game game) {
        this.sceneLoader = sceneLoader;
        this.clients = clients;
        this.games = games;
        this.selector = selector;
        this.selectorThread = selectorThread;
        this.client = client;
        this.clientPreferredName = clientPreferredName;
        this.isConnecting = isConnecting;
        this.isConnected = isConnected;
        this.failedToConnect = failedToConnect;
        this.betterColorsActiv = betterColorsActiv;
        this.quadrantIDs = quadrantIDs;
        this.game = game;
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
}
