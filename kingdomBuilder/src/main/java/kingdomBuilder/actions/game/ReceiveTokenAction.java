package kingdomBuilder.actions.game;

import kingdomBuilder.network.protocol.TokenReceived;
import kingdomBuilder.redux.Action;

public class ReceiveTokenAction extends Action {
    private TokenReceived payload;

    public ReceiveTokenAction(TokenReceived payload){
        this.payload = payload;
    }

    public TokenReceived getPayload() {
        return payload;
    }
}
