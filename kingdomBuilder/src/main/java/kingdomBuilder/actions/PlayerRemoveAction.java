package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.PlayerLeft;
import kingdomBuilder.redux.Action;

/**
 * Represents the PlayerRemoveAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains a field that stores the boolean and a constructor that sets the boolean.
 */
public class PlayerRemoveAction extends PlayerAction {

    /**
     * Creates a new PlayerRemoveAction with the id of the client and the game.
     * @param clientId the id of the client.
     * @param gameId the id of the game.
     */
    public PlayerRemoveAction(int clientId, int gameId) {
        super(clientId, gameId);
    }

    /**
     * Creates a new PlayerRemoveAction with a PlayerLeft message.
     * @param message a PlayerLeft message.
     */
    public PlayerRemoveAction(PlayerLeft message) {
        this(message.clientId(), message.gameId());
    }
}
