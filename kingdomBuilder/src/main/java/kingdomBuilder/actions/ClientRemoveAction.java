package kingdomBuilder.actions;

import kingdomBuilder.actions.base.ClientAction;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the ClientRemoveAction. Only triggered if another client left the server.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains the client id, the name and his gameId
 * </p>
 */
public class ClientRemoveAction extends ClientAction {
    /**
     * Constructs a new ClientRemoveAction.
     *
     * @param clientData the client data.
     */
    public ClientRemoveAction(ClientData clientData) {
        super(clientData);
    }
}
