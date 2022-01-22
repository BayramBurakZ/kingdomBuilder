package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

import java.util.List;

/**
 * <p>
 * Represents the ChatSendAction.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains two fields. One for saving the receiverIds in a List and one for the String that represents the
 * chat message.
 * </p>
 */
public class ChatSendAction extends Action {
    /**
     * A List that contains all receiverIds in form of Integers
     */
    public List<Integer> receiverIds;
    /**
     * A String that represents the actual chat message the User wants to send
     */
    public String message;

    /**
     * Constructor that creates a new ChatSendAction. Sets the internal fields for receiverIds to the given ones
     * and the field for the String that represents the message to the given one.
     * @param receiverIds a list with all the receiverIds in form of Integers
     * @param message string that represents the chatMessage to send to the server
     */
    public ChatSendAction(List<Integer> receiverIds, String message) {
        this.receiverIds = receiverIds;
        this.message = message;
    }
}
