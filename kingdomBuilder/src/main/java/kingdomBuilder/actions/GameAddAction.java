package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.redux.Action;

/**
 * Represents the GameAddAction.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class GameAddAction extends Action {
    /**
     * Represents the data for a game.
     */
    public GameData gameData;

    /**
     * Constructs a new GameAddAction.
     * @param gameData The game data.
     */
    public GameAddAction(GameData gameData) {
        this.gameData = gameData;
    }

    /**
     * Constructs a new GameAddAction.
     *
     * @param clientId The ID of the client.
     * @param gameType The type of game that is played.
     * @param gameId The ID of the game.
     * @param gameName The name of the game.
     * @param gameDescription The description.
     * @param playerLimit The limit of the players that are allowed in a game.
     * @param playersJoined The players that are joined.
     */
    public GameAddAction(int clientId,
                         String gameType,
                         int gameId,
                         String gameName,
                         String gameDescription,
                         int playerLimit,
                         int playersJoined) {
        this.gameData = new GameData(
                clientId,
                gameType,
                gameId,
                gameName,
                gameDescription,
                playerLimit,
                playersJoined);
    }
}
