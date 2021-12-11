package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

public class DisconnectAction extends Action {
    public boolean wasKicked;

    public DisconnectAction() {
        this.wasKicked = false;
    }

    public DisconnectAction(boolean wasKicked) {
        this.wasKicked = wasKicked;
    }
}
