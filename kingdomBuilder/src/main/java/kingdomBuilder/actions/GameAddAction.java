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
    public final GameData gameData;

    /**
     * Constructs a new GameAddAction.
     * @param gameData the game data.
     */
    public GameAddAction(GameData gameData) {
        this.gameData = gameData;
    }
}
