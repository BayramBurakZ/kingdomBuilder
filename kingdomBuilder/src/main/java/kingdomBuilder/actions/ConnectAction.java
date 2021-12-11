package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

public class ConnectAction extends Action {
    public String address;
    public int port;

    public ConnectAction(String address, int port) {
        this.address = address;
        this.port = port;
    }
}
