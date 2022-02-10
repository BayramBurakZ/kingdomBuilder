package kingdomBuilder.actions.game;

import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.redux.Action;

public class ActivateTokenAction extends Action {
    private Game.TileType token;

    public Game.TileType getToken() {
        return token;
    }

    public ActivateTokenAction(Game.TileType token) {
        this.token = token;
    }
}
