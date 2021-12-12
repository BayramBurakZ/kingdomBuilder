package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.server.ClientLeft;
import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the ClientRemoveAction. Only triggered if another client left the server.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains the client id, the name and his gameId
 * </p>
 */
public class ClientRemoveAction extends Action {
    public int id;
    public String name;
    public int gameId;

    /**
     * <p>
     * Constructor that creates a new ClientRemoveAction. Sets the fields according to the given
     * {@link kingdomBuilder.network.protocol.server.ClientLeft ClientLeft} Message that the server
     * has sent. Sets the clientId, name and the gameId to the values inside the ClientLeft-object.
     * </p>
     * @param c represents the message, that the server sent
     */
    public ClientRemoveAction(ClientLeft c) {
        this(c.clientId(), c.name(), c.gameId());
    }

    /**
     * <p>
     * Constructor that creates a new ClientRemoveAction. Sets the fields according to the given parameters.
     * </p>
     * @param id the id of the client that left the server
     * @param name the name of the client that left the server
     * @param gameId the gameId of the client that left the server
     */
    public ClientRemoveAction(int id, String name, int gameId) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
    }
}
