package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

/**
 * Represents the BetterColorModeAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class BetterColorModeAction extends Action {
    /**
     * The boolean to set the ColorMode.
     */
    public boolean active;

    /**
     * Constructor that creates a new BetterColorModeAction
     * that sets the betterColormode-field to the given state.
     * @param active the value, if it is on or off.
     */
    public BetterColorModeAction(boolean active) {
        this.active = active;
    }
}
