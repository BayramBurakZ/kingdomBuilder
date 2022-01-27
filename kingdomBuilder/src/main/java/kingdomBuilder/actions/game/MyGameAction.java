package kingdomBuilder.actions.game;

import kingdomBuilder.network.protocol.MyGameReply;
import kingdomBuilder.redux.Action;

/**
 * Represents the MyGameAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class MyGameAction extends Action {

    /**
     * Represents the MyGameReplay from the network.
     */
    public MyGameReply myGameReply;

    /**
     * Creates a new MyGameAction.
     * @param myGameReply the myGameReply message.
     */
    public MyGameAction(MyGameReply myGameReply) {
        this.myGameReply = myGameReply;
    }
}
