package kingdomBuilder.actions;

import kingdomBuilder.actions.base.PlayerAction;
import kingdomBuilder.network.protocol.PlayerJoined;
import kingdomBuilder.redux.Action;

/**
 * Represents the PlayerAddAction. Used for the {@link kingdomBuilder.redux.Store#dispatchOld(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run
 */
public class PlayerAddAction extends PlayerAction {

    /**
     * Creates a new PlayerAddAction with the mainClient id and game id.
     * @param clientId the id of the mainClient.
     * @param gameId the id of the game.
     */
    public PlayerAddAction(int clientId, int gameId) {
        super(clientId, gameId);
    }

    /**
     * Creates a new PlayerAddAction with a PlayerJoined message.
     * @param message the PlayerJoined Message.
     */
    public PlayerAddAction(PlayerJoined message) {
        this(message.clientId(), message.gameId());
    }
}
