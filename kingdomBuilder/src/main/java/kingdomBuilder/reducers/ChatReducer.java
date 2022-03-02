package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.chat.ChatSendAction;
import kingdomBuilder.redux.Reduce;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;
import kingdomBuilder.generated.DeferredState;
import kingdomBuilder.network.protocol.Message;

public class ChatReducer extends Reducer<KBState> {
    public static final String RECEIVE_MESSAGE = "RECEIVE_MESSAGE";
    public static final String SEND_MESSAGE = "SEND_MESSAGE";

    @Reduce(action = RECEIVE_MESSAGE)
    public DeferredState onReceiveMessage(Store<KBState> unused, KBState oldState, Message message) {
        DeferredState state = new DeferredState(oldState);
        state.setMessage(message);

        return state;
    }

    @Reduce(action = SEND_MESSAGE)
    public DeferredState reduce(Store<KBState> unused, KBState oldState, ChatSendAction a) {
        oldState.client.chat(a.receiverIds, a.message);
        return new DeferredState(oldState);
    }


}
