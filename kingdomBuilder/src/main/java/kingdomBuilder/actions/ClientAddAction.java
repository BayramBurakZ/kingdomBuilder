package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the ClientAddAction. Only triggered if another client joins the server.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 * Contains the client data
 * </p>
 */
public class ClientAddAction extends ClientAction {
    /**
     * Constructs a new ClientAddAction.
     *
     * @param clientData the client data.
     */
    public ClientAddAction(ClientData clientData) {
        super(clientData);
    }
}
