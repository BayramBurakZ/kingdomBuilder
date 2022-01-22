package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.redux.Action;

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
