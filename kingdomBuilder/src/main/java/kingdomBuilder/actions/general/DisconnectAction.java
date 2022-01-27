package kingdomBuilder.actions.general;

import kingdomBuilder.network.protocol.YouHaveBeenKicked;
import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the DisConnectAction.
 * Triggered if the User wants to disconnect from a server or if the server send the
 * {@link YouHaveBeenKicked YouHaveBeenKicked-Message}.
 * This Action has a wasKicked Boolean, that represents if the server has kicked you or the user left
 * voluntarily.
 * </p> <p>
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * </p>
 */
public class DisconnectAction extends Action {
    /**
     * says if the server kicked the client (true) or the user left voluntarily (false)
     */
    public boolean wasKicked;

    /**
     * Constructor that is called if the user left voluntarily. So the wasKicked-field is set to false.
     * With that the {@link kingdomBuilder.reducers.KBReducer Reducer} knows, that he handle it normally
     */
    public DisconnectAction() {
        this.wasKicked = false;
    }

    /**
     * Constructor for the DisconnectAction that sets the kicked-value to the given parameter.
     * With the parameter the {@link kingdomBuilder.reducers.KBReducer Reducer} knows, how to handle the
     * disconnect.
     * @param wasKicked if true the client was kicked from the server
     */
    public DisconnectAction(boolean wasKicked) {
        this.wasKicked = wasKicked;
    }
}
