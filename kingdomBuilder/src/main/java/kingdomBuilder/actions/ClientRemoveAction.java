package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.server.ClientLeft;
import kingdomBuilder.redux.Action;

public class ClientRemoveAction extends Action {
    public int id;
    public String name;
    public int gameId;

    public ClientRemoveAction(ClientLeft c) {
        this(c.clientId(), c.name(), c.gameId());
    }

    public ClientRemoveAction(int id, String name, int gameId) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
    }
}
