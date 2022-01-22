package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.PlayerJoined;

public class PlayerAddAction extends PlayerAction {

    public PlayerAddAction(int clientId, int gameId) {
        super(clientId, gameId);
    }

    public PlayerAddAction(PlayerJoined message) {
        this(message.clientId(), message.gameId());
    }
}
