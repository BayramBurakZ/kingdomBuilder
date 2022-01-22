package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

public abstract class PlayerAction extends Action {
    public final int clientId;
    public final int gameId;

    public PlayerAction(int clientId, int gameId) {
        this.clientId = clientId;
        this.gameId = gameId;
    }
}
