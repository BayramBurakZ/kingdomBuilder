package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.ClientJoined;
import kingdomBuilder.redux.Action;

public class ClientAddAction extends Action {
    public int id;
    public String name;
    public int gameId;

    public ClientAddAction(ClientJoined c) {
        this(c.clientId(), c.name(), c.gameId());
    }

    public ClientAddAction(int id, String name, int gameId) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
    }
}
