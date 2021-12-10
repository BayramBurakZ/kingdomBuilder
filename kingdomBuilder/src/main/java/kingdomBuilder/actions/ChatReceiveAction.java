package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.redux.Action;

import java.util.List;

public class ChatReceiveAction extends Action {
    public Message chatMessage;

    public ChatReceiveAction(Message chatMessage) {
        this.chatMessage = chatMessage;
    }
}
