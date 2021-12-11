package kingdomBuilder.actions;

import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Action;

public class SetMainClientAction extends Action {
    public Client client;

    public SetMainClientAction(Client client) { this.client = client; }
}
