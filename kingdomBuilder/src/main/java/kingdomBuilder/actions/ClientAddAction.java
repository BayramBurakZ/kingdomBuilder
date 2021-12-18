package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the ClientAddAction. Only triggered if another client joins the server.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains the client id, the name and his gameId
 * </p>
 */
public class ClientAddAction extends Action {
    /**
     * Id of the client that joined
     */
    public int id;
    /**
     * name of the client that joined
     */
    public String name;

    /**
     * gameI of the client that joined
     */
    public int gameId;

    /**
     * <p>
     * Constructor that creates a new ClientAddAction. Sets the fields according to the given parameters.
     * </p>
     * @param id the id of the client that joined the server
     * @param name the name of the client that joined the server
     * @param gameId the gameId of the client that joined the server
     */
    public ClientAddAction(int id, String name, int gameId) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
    }
}
