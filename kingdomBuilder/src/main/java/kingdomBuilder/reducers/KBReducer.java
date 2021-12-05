package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.ClientAddAction;
import kingdomBuilder.actions.IncrementAction;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Action;
import kingdomBuilder.redux.Reducer;

public class KBReducer implements Reducer<KBState> {

    @Override
    public KBState reduce(KBState oldState, Action action) {
        if (action instanceof ClientAddAction a) {
            KBState state = new KBState(oldState);
            state.clients.put(a.id, new ClientDAO(a.id, a.name, a.gameId));
            return state;
        }

        return oldState;
    }
}
