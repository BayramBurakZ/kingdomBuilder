package kingdomBuilder.actions.base;

import kingdomBuilder.redux.Action;

/**
 * Used as a base class for any action that handles player data.
 */
public abstract class PlayerAction extends Action {
    /**
     * Represents the id of a mainClient.
     */
    public final int clientId;
    /**
     * Represents the id of a game.
     */
    public final int gameId;

    /**
     * Creates a new PlayerAction.
     * @param clientId the id of a mainClient.
     * @param gameId the id of a game.
     */
    public PlayerAction(int clientId, int gameId) {
        this.clientId = clientId;
        this.gameId = gameId;
    }
}
