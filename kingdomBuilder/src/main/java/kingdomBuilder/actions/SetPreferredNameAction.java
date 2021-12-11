package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

public class SetPreferredNameAction extends Action {

    public String clientName;

    public SetPreferredNameAction(String clientName){
        this.clientName = clientName;
    }
}
