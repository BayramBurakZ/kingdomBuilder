package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

public class SetClientIDAction extends Action {
    public int clientID;

    public SetClientIDAction(int clientID) { this.clientID = clientID; }
}
