package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the ChatReceiveAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains a field that stores the actual message and a constructor that sets the message.
 * </p>
 */
public class ChatReceiveAction extends Action {
    /**
     * The actual incoming chat message
     */
    public Message chatMessage;

    /**
     * Constructor that creates a new ChatReceivedAction that sets the chatMessage-field to the given message
     * @param chatMessage the received chat message
     */
    public ChatReceiveAction(Message chatMessage) {
        this.chatMessage = chatMessage;
    }
}
