package kingdomBuilder.actions.game;

import kingdomBuilder.redux.Action;

/**
 * Represents the TurnStartAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class TurnEndAction extends Action {

    /**
     * Creates a new TurnEndAction with the given message.
     */
    public TurnEndAction() {}
}
