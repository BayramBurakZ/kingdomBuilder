package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

/**
 * <p>
 * Action to set the Users PreferredName in the {@link kingdomBuilder.KBState state} of the store.
 * It is used that the client has always his preferred named when joining a server. If there is already a client with
 * the same name, this name will not be overwritten. So for the next connect the client tries again with the
 * preferred name.
 * </p> <p>
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * </p>
 */
public class SetPreferredNameAction extends Action {
    /**
     * String that represents the preferred name of the client
     */
    public String clientName;

    /**
     * Constructor that creates a new SetPreferredNameAction.
     * @param clientName string that represents the PreferredName for the client
     */
    public SetPreferredNameAction(String clientName){
        this.clientName = clientName;
    }
}
