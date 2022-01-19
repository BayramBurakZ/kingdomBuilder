package kingdomBuilder.actions;

import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Action;

/**
 * Represents the LoggedInAction.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class LoggedInAction extends Action {
    /**
     * Represents the client.
     */
    public final Client client;

    /**
     * Constructs a new LoggedInAction.
     * @param client The client.
     */
    public LoggedInAction(Client client) {
        this.client = client;
    }
}
