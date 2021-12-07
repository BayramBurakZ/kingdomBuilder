package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.ClientAddAction;
import kingdomBuilder.actions.ClientRemoveAction;
import kingdomBuilder.actions.SetClientIDAction;
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

        if (action instanceof SetClientIDAction a) {
            KBState state = new KBState(oldState);
            state.clientID = a.clientID;
            return state;
        }

        if (action instanceof SetClientNameAction a) {
            KBState state = new KBState(oldState);
            state.clientName = a.clientName;
            return state;
        }

        return oldState;
    }
}
