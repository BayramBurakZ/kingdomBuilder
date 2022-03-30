package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.chat.ChatSendAction;
import kingdomBuilder.redux.Reduce;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;
import kingdomBuilder.generated.DeferredState;
import kingdomBuilder.network.protocol.Message;

/**
 * This Reducer handles everything about chat messages.
 */
public class ChatReducer extends Reducer<KBState> {
    /**
     * Represents the String to identify the related {@link ChatReducer#onReceiveMessage reduce} method.
     */
    public static final String RECEIVE_MESSAGE = "RECEIVE_MESSAGE";
    /**
     * Represents the String to identify the related {@link ChatReducer#onSendMessage reduce} method.
     */
    public static final String SEND_MESSAGE = "SEND_MESSAGE";

    /**
     * Constructs a new RootReducer and lets it register itself.
     * @see Reducer#registerReducers
     */
    public ChatReducer() { registerReducers(this); }

    /**
     * Represents the reducer to handle an incoming message.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param message the incoming chat message.
     *
     * @return the deferredState.
     */
    @Reduce(action = RECEIVE_MESSAGE)
    public DeferredState onReceiveMessage(Store<KBState> unused, KBState oldState, Message message) {
        DeferredState state = new DeferredState(oldState);
        state.setMessage(message);

        return state;
    }

    /**
     * Represents the reducer to send a chat message.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param sendAction the chat message to send.
     *
     * @return the deferredState.
     */
    @Reduce(action = SEND_MESSAGE)
    public DeferredState onSendMessage(Store<KBState> unused, KBState oldState, ChatSendAction sendAction) {
        oldState.mainClient().chat(sendAction.receiverIds, sendAction.message);
        return new DeferredState(oldState);
    }
}
