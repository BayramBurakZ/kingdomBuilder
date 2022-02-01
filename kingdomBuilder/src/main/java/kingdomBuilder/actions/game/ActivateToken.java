package kingdomBuilder.actions.game;

import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.redux.Action;

public class ActivateToken extends Action {
    private Game.TileType token;

    public Game.TileType getToken() {
        return token;
    }

    public ActivateToken(Game.TileType token) {
        this.token = token;
    }
}
