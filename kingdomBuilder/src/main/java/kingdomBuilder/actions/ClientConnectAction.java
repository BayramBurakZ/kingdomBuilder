package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

public class ClientConnectAction extends Action {
    public String address;
    public int port;

    public ClientConnectAction(String address, int port) {
        this.address = address;
        this.port = port;
    }
}
