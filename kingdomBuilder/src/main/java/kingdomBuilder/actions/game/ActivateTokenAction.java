package kingdomBuilder.actions.game;

import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.redux.Action;

public class ActivateTokenAction extends Action {
    private TileType token;

    public TileType getToken() {
        return token;
    }

    public ActivateTokenAction(TileType token) {
        this.token = token;
    }
}
