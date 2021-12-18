package kingdomBuilder.actions;

import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Action;

public class LoggedInAction extends Action {
    public final Client client;

    public LoggedInAction(Client client) {
        this.client = client;
    }
}
