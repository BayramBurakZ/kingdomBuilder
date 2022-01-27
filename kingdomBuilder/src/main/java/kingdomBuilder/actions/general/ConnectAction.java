package kingdomBuilder.actions.general;

import kingdomBuilder.redux.Action;

import java.net.InetSocketAddress;

/**
 * Represents the ConnectAction. Only triggered if this client wants to connect to a server.
 * Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 */
public class ConnectAction extends Action {

    /**
     * Stores the address to connect to.
     */
    public final InetSocketAddress address;

    /**
     * Creates a new ConnectAction with the given address.
     * @param address hte address.
     */
    public ConnectAction(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Creates a new ConnectAction. Sets the own fields to the value given: Sets the address to the address parameter
     * and the port to the port parameter
     * @param address string that represents the address of the server that we want to connect to
     * @param port int that represents the port for the server connection
     */
    public ConnectAction(String address, int port) {
        this.address = new InetSocketAddress(address, port);
    }
}
