package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.redux.Reduce;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import kingdomBuilder.generated.DeferredState;

/**
 * This Reducer handles everything about root messages.
 */
public class RootReducer extends Reducer<KBState> {
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String SEND_ROOT = "SEND_ROOT";
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String ON_ROOT = "ON_ROOT";
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String WRONG_PASSWORD = "WRONG_PASSWORD";
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String KICK_CLIENT = "KICK_CLIENT";
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String RESET_STATUS = "RESET_STATUS";
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String SHUTDOWN_SERVER = "SHUTDOWN_SERVER";

    /**
     * Constructs a new RootReducer and lets it register itself.
     */
    public RootReducer() {
        registerReducers(this);
    }

    /**
     * Represents the reducer to send the 'root' message with the given password.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param password the password to send.
     *
     * @return the deferredState.
     */
    @Reduce(action = SEND_ROOT)
    public DeferredState sendRoot(Store<KBState> unused, KBState oldState, String password) {
        oldState.client().root(password);
        return new DeferredState(oldState);
    }

    /**
     * Represents the reducer to react when the error message comes in.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an object that is unused in this case.
     *
     * @return the deferredState.
     */
    @Reduce(action = WRONG_PASSWORD)
    public DeferredState onWrongPassword(Store<KBState> unused, KBState oldState, Object unused2) {
        DeferredState state = new DeferredState(oldState);

        state.setClientState(KBState.ClientState.ERROR);
        return state;
    }

    /**
     * Represents the reducer to react when the 'you are root' message comes in.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an object that is unused in this case.
     *
     * @return the deferredState.
     */
    @Reduce(action = ON_ROOT)
    public DeferredState onRoot(Store<KBState> unused, KBState oldState, Object unused2) {
        DeferredState state = new DeferredState(oldState);
        state.setClientState(KBState.ClientState.ROOT);
        return state;
    }

    /**
     * Represents the reducer to send a 'kick client' message to the server.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param integer the id of the client to kick.
     *
     * @return the deferredState.
     */
    @Reduce(action = KICK_CLIENT)
    public DeferredState kickClient(Store<KBState> unused, KBState oldState, Integer integer) {
        oldState.client().kickClient(integer);
        return new DeferredState(oldState);
    }

    /**
     * Represents the reducer to reset the client status in the state after an error.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an object that is unused in this case.
     *
     * @return the deferredState.
     */
    @Reduce(action = RESET_STATUS)
    public DeferredState resetStatus(Store<KBState> unused, KBState oldState, Object unused2) {
        DeferredState state = new DeferredState(oldState);
        state.setClientState(KBState.ClientState.NO_ROOT);
        return state;
    }

    /**
     * Represents the reducer to send the 'shutdown server' message.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an object that is unused in this case.
     *
     * @return the deferredState.
     */
    @Reduce(action = SHUTDOWN_SERVER)
    public DeferredState shutdownServer(Store<KBState> unused, KBState oldState, Object unused2) {
        oldState.client().shutdownServer();
        return new DeferredState(oldState);
    }
}
