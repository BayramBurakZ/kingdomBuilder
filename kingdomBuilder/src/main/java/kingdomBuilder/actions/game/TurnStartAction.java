package kingdomBuilder.actions.game;

import kingdomBuilder.network.protocol.TurnStart;
import kingdomBuilder.redux.Action;

/**
 * Represents the TurnStartAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class TurnStartAction extends Action {

    /**
     * Represents the network message.
     */
    public TurnStart turnStart;

    /**
     * Constructs a new TurnStartAction
     * @param turnStart the network message for a turn start.
     */
    public TurnStartAction(TurnStart turnStart) {
        this.turnStart = turnStart;
    }
}
