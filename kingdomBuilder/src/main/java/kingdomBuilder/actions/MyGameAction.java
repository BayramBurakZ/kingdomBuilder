package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.MyGameReply;
import kingdomBuilder.redux.Action;

/**
 * Represents the MyGameAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class MyGameAction extends Action {

    public MyGameReply myGameReply;

    public MyGameAction(MyGameReply myGameReply) {
        this.myGameReply = myGameReply;
    }
}
