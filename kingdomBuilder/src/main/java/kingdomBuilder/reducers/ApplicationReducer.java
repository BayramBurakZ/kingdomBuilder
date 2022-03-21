package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.gamelogic.ServerTurn;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.generated.DeferredState;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.network.protocol.QuadrantUploaded;
import kingdomBuilder.network.protocol.VersionReply;
import kingdomBuilder.redux.Reduce;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Handles all application related actions.
 */
public class ApplicationReducer extends Reducer<KBState> {
    public static final String EXIT_APPLICATION = "EXIT_APPLICATION";
    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String ADD_CLIENT = "ADD_CLIENT";
    public static final String REMOVE_CLIENT = "REMOVE_CLIENT";
    public static final String LOGIN = "LOGIN";
    public static final String SET_SCENELOADER = "SET_SCENELOADER";
    public static final String NEW_QUADRANT_UPLOADED = "NEW_QUADRANT_UPLOADED";
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String NAMESPACE_LOADED = "NAMESPACE_LOADED";
    /**
     * Represents the String to identify the related reduce methode.
     */
    public static final String SERVER_VERSION = "SERVER_VERSION";


    public ApplicationReducer() { registerReducers(this);}

    @Reduce(action = EXIT_APPLICATION)
    public DeferredState onExitApplication(Store<KBState> unused, KBState state, Object unused2) {
        System.out.println("Exiting application!");
        ClientSelector selector = state.selector();
        if (selector != null && selector.isRunning()) selector.stop();

        Thread selectorThread = state.selectorThread();
        if (selectorThread != null && selectorThread.isAlive()) selectorThread.interrupt();

        // Return old state, so that no other subscribers are called.
        return new DeferredState(state);
    }

    @Reduce(action = CONNECT)
    public DeferredState onConnect(Store<KBState> store, KBState oldState, InetSocketAddress address) {
        DeferredState state = new DeferredState(oldState);

        Client client;
        try {
            client = oldState.selector().connect(address);
        } catch (IOException exc) {
            state.setFailedToConnect(true);
            return state;
        }

        state.setServerAddress(address);

        Thread selectorThread = oldState.selectorThread();
        if (selectorThread == null || !selectorThread.isAlive()) {
            assert !oldState.selector().isRunning();

            selectorThread = new Thread(oldState.selector());
            selectorThread.setName("SelectorThread");
            selectorThread.start();

            state.setSelectorThread(selectorThread);
        }

        client.onVersionReply.subscribe(m -> store.dispatch(SERVER_VERSION, m));
        client.onLoggedIn.subscribe(m -> store.dispatch(LOGIN, m));
        client.onClientsReply.subscribe(m -> m.clients().forEach(c -> store.dispatch(ADD_CLIENT, c)));
        client.onClientJoined.subscribe(m -> store.dispatch(ADD_CLIENT, m.clientData()));
        client.onClientLeft.subscribe(m -> store.dispatch(REMOVE_CLIENT, m.clientData()));
        client.onQuadrantReply.subscribe(m -> store.dispatchOld(new QuadrantAddAction(m)));
        client.onPlayerJoined.subscribe(m -> store.dispatchOld(new PlayerAddAction(m)));
        client.onPlayerLeft.subscribe(m -> store.dispatchOld(new PlayerRemoveAction(m)));
        client.onGameHosted.subscribe(m -> store.dispatch(GameReducer.ADD_GAME, m.gameData()));
        client.onPlayersReply.subscribe(m -> store.dispatch(GameReducer.SET_PLAYERS, m));
        client.onMyGameReply.subscribe(m -> store.dispatch(GameReducer.MY_GAME, m));
        client.onWinCondition.subscribe(m -> store.dispatch(GameReducer.SET_WIN_CONDITION, m));
        client.onTurnStart.subscribe(m -> store.dispatch(GameReducer.START_TURN, m));
        client.onTerrainTypeOfTurn.subscribe(m -> store.dispatch(GameReducer.TERRAIN_OF_TURN, m));
        client.onTokenReceived.subscribe(m -> store.dispatch(GameReducer.GRANT_TOKEN, m));
        client.onTokenLost.subscribe(m -> store.dispatch(GameReducer.REVOKE_TOKEN, m));
        client.onScores.subscribe(m -> store.dispatch(GameReducer.SCORE, m));
        client.onPlayersOfGameReply.subscribe(m -> store.dispatch(GameReducer.PLAYERS_OF_GAME, m));
        client.onNamespaceLoaded.subscribe(m -> store.dispatch(NAMESPACE_LOADED, null));
        // root stuff
        client.onWrongPassword.subscribe(m -> store.dispatch(RootReducer.WRONG_PASSWORD, null));
        client.onYouAreRoot.subscribe(m -> store.dispatch(RootReducer.ON_ROOT, null));


        client.onGamesReply.subscribe(m -> {
            for (GameData g : m.games()) {
                store.dispatch(
                    GameReducer.ADD_GAME,
                    new GameData(g.clientId(), g.gameType(), g.gameId(), g.gameName(),
                                g.gameDescription(), g.playerLimit(), g.playersJoined())
                );
            }
        });

        client.onQuadrantsReply.subscribe(m -> {
            for (int quadrantId : m.quadrantIds()) {
                client.quadrantRequest(quadrantId);
            }
        });

        client.onWelcomeToGame.subscribe(m -> {
            client.myGameRequest();
            client.playersRequest();
        });

        client.onSettlementPlaced.subscribe(m -> store.dispatch(
                GameReducer.SERVER_TURN,
                new ServerTurn(m.clientId(), ServerTurn.TurnType.PLACE, m.row(), m.column(), -1, -1))
        );

        client.onSettlementRemoved.subscribe(m -> store.dispatch(
                GameReducer.SERVER_TURN,
                new ServerTurn(m.clientId(), ServerTurn.TurnType.REMOVE, m.row(), m.column(), -1, -1))
        );

        client.onTokenUsed.subscribe(m -> {
            TileType token = TileType.valueOf(m.tokenType());
            if (token == TileType.PADDOCK || token == TileType.BARN || token == TileType.HARBOR) {
                store.dispatch(
                    GameReducer.SERVER_TURN,
                    new ServerTurn(m.clientId(), ServerTurn.TurnType.TOKEN_USED, -1, -1, -1, -1)
                );
            }
        });

        store.subscribe(kbState -> store.dispatch(GameReducer.READY_GAME, null),
                "players", "nextTerrainCard", "nextPlayer");

        client.login(oldState.clientPreferredName());

        client.onQuadrantUploaded.subscribe(m ->{
            store.dispatch(NEW_QUADRANT_UPLOADED, m);
            client.quadrantRequest(m.quadrantId());
        });

        client.onMessageReceived.subscribe(m -> store.dispatch(ChatReducer.RECEIVE_MESSAGE, m));
        state.setClient(client);
        state.setIsConnecting(true);

        return state;
    }

    @Reduce(action = DISCONNECT)
    public DeferredState onDisconnect(Store<KBState> unused, KBState oldState, Boolean wasKicked) {
        DeferredState state = new DeferredState(oldState);

        // TODO: remove sceneloader/controller
        if (wasKicked) {
            var sceneLoader = oldState.sceneLoader();
            sceneLoader.getChatViewController().onYouHaveBeenKicked();
        }

        oldState.client().disconnect();
        state.setClient(null);
        state.setIsConnected(false);

        oldState.clients().clear();
        state.setClients(oldState.clients());

        oldState.games().clear();
        state.setGames(oldState.games());

        return state;
    }

    @Reduce(action = LOGIN)
    public DeferredState onLogin(Store<KBState> unused, KBState oldState, Object unused2) {
        DeferredState state = new DeferredState(oldState);
        state.setIsConnecting(false);
        state.setIsConnected(true);
        System.out.println("Is connected.");

        oldState.client().serverVersion();
        oldState.client().loadNamespace();
        oldState.client().clientsRequest();
        oldState.client().gamesRequest();
        return state;
    }

    @Reduce(action = ADD_CLIENT)
    public DeferredState onAddClient(Store<KBState> unused, KBState oldState, ClientData payload) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        clients.put(payload.clientId(), payload);
        state.setClients(clients);

        return state;
    }

    @Reduce(action = REMOVE_CLIENT)
    public DeferredState onRemoveClient(Store<KBState> unused, KBState oldState, ClientData payload) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        clients.remove(payload.clientId());
        state.setClients(clients);

        return state;
    }

    @Reduce(action = SET_SCENELOADER)
    public DeferredState onSetSceneLoader(Store<KBState> unused, KBState oldState, SceneLoader sceneLoader) {
        DeferredState state = new DeferredState(oldState);
        state.setSceneLoader(sceneLoader);
        return state;
    }

    @Reduce(action = NEW_QUADRANT_UPLOADED)
    public DeferredState onNewQuadrantUploaded(Store<KBState> unused, KBState oldState, QuadrantUploaded payload) {
        DeferredState state = new DeferredState(oldState);
        state.setQuadrantUploaded(payload);
        return state;
    }

    /**
     * Represents the reducer to set the version of the server in the state.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param version the VersionReply with the server version.
     *
     * @return the deferredState that modifies the state.
     */
    @Reduce(action = SERVER_VERSION)
    public DeferredState onServerVersion(Store<KBState> unused, KBState oldState, VersionReply version) {
        DeferredState state = new DeferredState(oldState);
        state.setServerVersion(version.serverVersion());
        return state;
    }

    /**
     * Represents the reducer to send the '?quadrants' message always after the namespace is loaded.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an object that is unused in this case.
     *
     * @return the deferredState.
     */
    @Reduce(action = NAMESPACE_LOADED)
    public DeferredState onNameSpaceLoaded(Store<KBState> unused, KBState oldState, Object unused2) {
        //This prevents the bug, that the server processes the '?quadrants' message before
        // it starts to load the namespace.
        oldState.client().quadrantsRequest();
        return new DeferredState(oldState);
    }
}
