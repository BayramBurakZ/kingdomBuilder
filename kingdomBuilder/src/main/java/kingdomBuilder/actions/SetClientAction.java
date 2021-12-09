package kingdomBuilder.actions;

import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Action;

public class SetClientAction extends Action {
    public Client client;

    public SetClientAction(Client client) { this.client = client; }
}
