package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

/**
 * <p>
 * Represents the ConnectAction. Only triggered if this client wants to connect to a server.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 * Contains the String that represents the server address and the server port as an int.
 * </p>
 */
public class ConnectAction extends Action {
    /**
     * String that represents the server address
     */
    public String address;
    /**
     * int that contains the port
     */
    public int port;

    /**
     * Creates a new ConnectAction. Sets the own fields to the value given: Sets the address to the address parameter
     * and the port to the port parameter
     * @param address String that represents the address of the server that we want to connect to
     * @param port int that represents the port for the server connection
     */
    public ConnectAction(String address, int port) {
        this.address = address;
        this.port = port;
    }
}
