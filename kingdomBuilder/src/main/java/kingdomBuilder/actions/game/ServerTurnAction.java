package kingdomBuilder.actions.game;

import kingdomBuilder.actions.base.TurnAction;
import kingdomBuilder.gamelogic.ServerTurn;

/**
 * Class that represents an action of a turn that the server sends.
 *
 * Represents the ServerTurnAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(kingdomBuilder.redux.Action)
 * dispatch()}-methode in the {@link kingdomBuilder.redux.Store Store} so the reducer
 * knows what type of action he needs to run.
 */
public class ServerTurnAction extends TurnAction {

    /**
     * Represents the ServerTurn.
     */
    public final ServerTurn turn;

    /**
     * Creates a new ServerTurnAction.
     * @param turn sets the turn for this action.
     */
    public ServerTurnAction(ServerTurn turn) {
        this.turn = turn;
    }
}
