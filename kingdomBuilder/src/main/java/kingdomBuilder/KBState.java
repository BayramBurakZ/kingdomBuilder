package kingdomBuilder;

import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gamelogic.Player;
import kingdomBuilder.gamelogic.Turn;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.internal.ClientSelectorImpl;
import kingdomBuilder.annotations.State;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.network.protocol.Message;
import kingdomBuilder.network.protocol.Scores;

import java.io.IOException;
import java.util.*;

/**
 * Represents the state of the Kingdom Builder application.
 */
@State
public class KBState {
    /**
     * Represents the SceneLoader.
     */
    public SceneLoader sceneLoader;

    /**
     * Maps client id to the client info of connected clients.
     */
    public final Map<Integer, ClientData> clients;

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
    public boolean betterColorsActive = false;

    /**
     * Represents a Map with all the available quadrants on the server;
     */
    public final Map<Integer, Game.TileType[]> quadrants;

    /**
     * Represents the game.
     */
    public Game game;

    /**
     * Represents the last turn that was taken in the current game.
     */
    public Turn gameLastTurn;

    /**
     * The terrain card of the next player's turn.
     */
    public Game.TileType nextTerrainCard;

    /**
     * The player whose turn it is next.
     */
    public int nextPlayer;

    public boolean gameStarted;

    public List<Player> players;

    public Game.TileType token;

    public Scores scores;

    public Message message;

    public KBState(SceneLoader sceneLoader,
                   Map<Integer, ClientData> clients,
                   Map<Integer, GameData> games,
                   ClientSelector selector,
                   Thread selectorThread,
                   Client client,
                   String clientPreferredName,
                   boolean isConnecting,
                   boolean isConnected,
                   boolean failedToConnect,
                   boolean betterColorsActive,
                   Map<Integer, Game.TileType[]> quadrants,
                   Game game,
                   Turn gameLastTurn,
                   Game.TileType nextTerrainCard,
                   int nextPlayer,
                   boolean gameStarted,
                   List<Player> players,
                   Game.TileType token,
                   Scores scores,
                   Message message) {
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
        this.betterColorsActive = betterColorsActive;
        this.quadrants = quadrants;
        this.game = game;
        this.gameLastTurn = gameLastTurn;
        this.nextTerrainCard = nextTerrainCard;
        this.nextPlayer = nextPlayer;
        this.gameStarted = gameStarted;
        this.players = players;
        this.token = token;
        this.scores = scores;
        this.message = message;
    }

    //TODO: JavaDoc!
    /**
     * Initializes the state with initial value.
     *
     * @throws IOException if the selector has troubles.
     */
    public KBState() throws IOException {
        clients = new HashMap<>();
        games = new HashMap<>();
        quadrants = new HashMap<>();
        selector = new ClientSelectorImpl();
        isConnecting = false;
        isConnected = false;
        failedToConnect = false;
        game = null;
        gameLastTurn = null;
        nextTerrainCard = null;
        nextPlayer = -1;
        gameStarted = false;
        token = null;
        scores = null;
        message = null;
    }
}
