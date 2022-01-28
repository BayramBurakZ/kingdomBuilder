package kingdomBuilder.actions.game;

import kingdomBuilder.actions.base.TurnAction;
import kingdomBuilder.gamelogic.ClientTurn;

/**
 * Class that represents an action of a turn that the client sends.
 *
 * Represents the ClientTurnAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(kingdomBuilder.redux.Action)
 * dispatch()}-methode in the {@link kingdomBuilder.redux.Store Store} so the reducer
 * knows what type of action he needs to run.
 */
public class ClientTurnAction extends TurnAction {
    /**
     * Represents the turn of the client.
     */
    public final ClientTurn turn;

    /**
     * Creates a new ClientTurnAction with the given turn.
     * @param turn the turn of the client.
     */
    public ClientTurnAction(ClientTurn turn) {
        this.turn = turn;
    }
}
