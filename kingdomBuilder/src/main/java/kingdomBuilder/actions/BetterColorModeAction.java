package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the BetterColorModeAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains a field that stores the boolean and a constructor that sets the boolean.
 * </p>
 */
public class BetterColorModeAction extends Action {
    /**
     * The boolean to set the ColorMode.
     */
    public boolean active;

    /**
     * Constructor that creates a new BetterColorModeAction
     * that sets the betterColormode-field to the given state.
     * @param active The value, if it is on or off.
     */
    public BetterColorModeAction(boolean active) {
        this.active = active;
    }
}
