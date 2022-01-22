package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.PlayerLeft;

public class PlayerRemoveAction extends PlayerAction {

    public PlayerRemoveAction(int clientId, int gameId) {
        super(clientId, gameId);
    }

    public PlayerRemoveAction(PlayerLeft message) {
        this(message.clientId(), message.gameId());
    }
}
