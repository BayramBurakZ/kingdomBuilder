package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.redux.Action;

/**
 * Represents the ClientAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains a field that stores the boolean and a constructor that sets the boolean.
 */
public abstract class ClientAction extends Action {
    /**
     * Represents the data for a client.
     */
    public final ClientData clientData;

    /**
     * Constructs a new ClientAction.
     * @param clientData the client data.
     */
    public ClientAction(ClientData clientData) {
        this.clientData = clientData;
    }
}
