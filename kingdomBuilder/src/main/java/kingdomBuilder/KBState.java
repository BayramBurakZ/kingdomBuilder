package kingdomBuilder;

import kingdomBuilder.annotations.State;
import kingdomBuilder.gamelogic.WinCondition;
import kingdomBuilder.gamelogic.*;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.internal.ClientSelectorImpl;
import kingdomBuilder.network.protocol.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the state of the Kingdom Builder application.
 *
 * @param sceneLoader         represents the SceneLoader.
 * @param clients             maps client id to the client info of connected clients.
 * @param games               maps game id to the game info of created games.
 * @param selector            stores the selector, which handles network IO.
 * @param selectorThread      stores the thread, which runs the selector.
 * @param client              represents the main client.
 * @param clientPreferredName represents the login name entered by the player.
 * @param isConnecting        shows whether the client is currently connecting or not.
 * @param isConnected         shows whether the client is connected to the server.
 * @param failedToConnect     shows whether the connection to the server failed.
 * @param betterColorsActive  shows whether the better color mode is active.
 * @param quadrants           represents a map with all the available quadrants on the server.
 * @param gameLastTurn        represents the last turn that was taken in the current game.
 * @param nextTerrainCard     represents the terrain card of the current player's turn.
 * @param nextPlayer          shows the player whose turn it is next.
 *                            It represents the ClientID.
 * @param gameStarted         represents the state if a game is running.
 * @param players             represents a list of players in the current game.
 * @param token               represents the active token.
 * @param scores              represents the scores message from the network.
 * @param message             represents an incoming chat-message.
 * @param gameMap             represents the internal data of the map.
 * @param myGameReply         represents the network message with all information about the current game.
 * @param winConditions       represents a list of the three win conditions of the current game.
 * @param playersMap          represents a map of the players playing in the game.
 *                            The Key represents the client ID.
 * @param currentPlayer       represents the current player on turn.
 * @param joinedGame          shows whether the client has joined a game.
 * @param quadrantUploaded    represents the message whenever a quadrant is uploaded.
 * @param serverVersion       represents a String with the version of the server.
 * @param playersOfGame       represents a map with the gameID and a list with all clientIDs.
 * @param serverAddress       represents the server address.
 * @param Bots                represents the Map for the bots.
 * @param clientState         represents the state of the client (Root or non-Root).
 */
@State
public record KBState(SceneLoader sceneLoader,
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
                      Map<Integer, TileType[]> quadrants,
                      Turn gameLastTurn,
                      TileType nextTerrainCard,
                      int nextPlayer,
                      boolean gameStarted,
                      ArrayList<Player> players,
                      TileType token,
                      Scores scores,
                      Message message,
                      GameMap gameMap,
                      MyGameReply myGameReply,
                      ArrayList<WinCondition> winConditions,
                      HashMap<Integer, Player> playersMap,
                      Player currentPlayer,
                      boolean joinedGame,
                      QuadrantUploaded quadrantUploaded,
                      String serverVersion,
                      Map<Integer, List<Integer>> playersOfGame,
                      InetSocketAddress serverAddress,
                      Map<Client, AIGame> Bots,
                      ClientState clientState,
                      int turnCount
) {

    /**
     * Initializes the state with initial value.
     *
     * @throws IOException when something goes wrong.
     */
    public KBState() throws IOException {
        this(null,
                new HashMap<>(),
                new HashMap<>(),
                new ClientSelectorImpl(),
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                new HashMap<>(),
                null,
                null,
                -1,
                false,
                new ArrayList<>(),
                null,
                null,
                null,
                null,
                null,
                new ArrayList<>(),
                new HashMap<>(),
                null,
                false,
                null,
                null,
                new HashMap<>(),
                null,
                new HashMap<>(),
                ClientState.NO_ROOT,
                0
        );
    }

    /**
     * Represents the clientStatus.
     */
    public enum ClientState{
        /**
         * Represents the status when the client has not gained the root status yet.
         */
        NO_ROOT,
        /**
         * Represents the status when the client has gained the root status.
         */
        ROOT,
        /**
         * Represents the status when the client tried to get the root status but failed with a wrong password.
         */
        ERROR
    }
}
