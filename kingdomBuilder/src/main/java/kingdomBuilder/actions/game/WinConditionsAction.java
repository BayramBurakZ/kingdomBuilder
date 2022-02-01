package kingdomBuilder.actions.game;

import kingdomBuilder.network.protocol.WinCondition;
import kingdomBuilder.redux.Action;

/**
 * Represents the WinConditionsAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class WinConditionsAction extends Action {

    /**
     * Represents the winConditionReply from the network.
     */
    public WinCondition winConditionReply;

    /**
     * Constructs a new StartGameAction
     * @param winConditionReply the network message for a winCondition.
     */
    public WinConditionsAction(WinCondition winConditionReply) {
        this.winConditionReply = winConditionReply;
    }
}
