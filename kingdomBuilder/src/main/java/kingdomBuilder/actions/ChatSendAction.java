package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

import java.util.List;

public class ChatSendAction extends Action {
    public List<Integer> receiverIds;
    public String message;

    public ChatSendAction(List<Integer> receiverIds, String message) {
        this.receiverIds = receiverIds;
        this.message = message;
    }
}
