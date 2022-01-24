package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.PlayersReply;
import kingdomBuilder.redux.Action;

/**
 * Represents the SetPlayersAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 */
public class SetPlayersAction extends Action {
    /**
     * Represents the PlayersReply message.
     */
    public final PlayersReply playersReply;

    /**
     * Creates a new SetPlayerAction with the given message.
     * @param playersReply the playerReply message.
     */
    public SetPlayersAction(PlayersReply playersReply) {
        this.playersReply = playersReply;
    }
}
