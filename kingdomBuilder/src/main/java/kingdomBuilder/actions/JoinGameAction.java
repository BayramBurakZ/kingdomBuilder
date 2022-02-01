package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

/**
 * Represents the JoinGameAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains a field that stores the boolean and a constructor that sets the boolean.
 */
public class JoinGameAction extends Action {
    /**
     * Represents the id of the game.
     */
    public final int gameId;

    /**
     * Creates a new JoinGameAction with the given id.
     * @param gameId the id of the game.
     */
    public JoinGameAction(int gameId) {
        this.gameId = gameId;
    }
}
