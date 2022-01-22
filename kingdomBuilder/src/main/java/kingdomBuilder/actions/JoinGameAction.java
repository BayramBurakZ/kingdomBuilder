package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

//TODO: Javadoc!
public class JoinGameAction extends Action {
    public final int gameId;

    public JoinGameAction(int gameId) {
        this.gameId = gameId;
    }
}
