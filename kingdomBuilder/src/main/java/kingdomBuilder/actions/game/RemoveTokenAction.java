package kingdomBuilder.actions.game;

import kingdomBuilder.network.protocol.TokenLost;
import kingdomBuilder.redux.Action;

public class RemoveTokenAction extends Action {
    private TokenLost payload;

    public RemoveTokenAction(TokenLost payload){
        this.payload = payload;
    }

    public TokenLost getPayload() {
        return payload;
    }
}
