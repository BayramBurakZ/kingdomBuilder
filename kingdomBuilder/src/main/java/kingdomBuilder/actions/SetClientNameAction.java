package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

public class SetClientNameAction extends Action {

    public String clientName;

    public SetClientNameAction(String clientName){
        this.clientName = clientName;
    }
}
