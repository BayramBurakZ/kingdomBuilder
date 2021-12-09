package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.ClientAddAction;
import kingdomBuilder.actions.ClientRemoveAction;
import kingdomBuilder.actions.SetClientAction;
import kingdomBuilder.actions.SetClientNameAction;
import kingdomBuilder.model.ClientDAO;
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

        if (action instanceof ClientRemoveAction a) {
            KBState state = new KBState(oldState);
            state.clients.remove(a.id);
            return state;
        }

        if (action instanceof SetClientAction a) {
            KBState state = new KBState(oldState);
            state.client = a.client;
            return state;
        }

        if (action instanceof SetClientNameAction a) {
            KBState state = new KBState(oldState);
            state.clientPreferredName = a.clientName;
            return state;
        }

        return oldState;
    }
}
