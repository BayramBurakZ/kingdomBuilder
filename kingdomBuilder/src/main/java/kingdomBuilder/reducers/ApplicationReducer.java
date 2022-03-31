package kingdomBuilder.reducers;

import javafx.stage.Stage;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.gamelogic.ServerTurn;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.generated.DeferredState;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.gui.util.Util;
import kingdomBuilder.misc.Server;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.network.protocol.QuadrantUploaded;
import kingdomBuilder.network.protocol.VersionReply;
import kingdomBuilder.redux.Reduce;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Handles all application related actions.
 */
public class ApplicationReducer extends Reducer<KBState> {
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onExitApplication reduce} method.
     */
    public static final String EXIT_APPLICATION = "EXIT_APPLICATION";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onConnect reduce} method.
     */
    public static final String CONNECT = "CONNECT";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onDisconnect reduce} method.
     */
    public static final String DISCONNECT = "DISCONNECT";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onAddClient reduce} method.
     */
    public static final String ADD_CLIENT = "ADD_CLIENT";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onRemoveClient reduce} method.
     */
    public static final String REMOVE_CLIENT = "REMOVE_CLIENT";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onLogin reduce} method.
     */
    public static final String LOGIN = "LOGIN";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onSetSceneLoader reduce} method.
     */
    public static final String SET_SCENELOADER = "SET_SCENELOADER";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onNewQuadrantUploaded reduce} method.
     */
    public static final String NEW_QUADRANT_UPLOADED = "NEW_QUADRANT_UPLOADED";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onNameSpaceLoaded reduce} method.
     */
    public static final String NAMESPACE_LOADED = "NAMESPACE_LOADED";
    /**
     * Represents the String to identify the related {@link ApplicationReducer#onServerVersion reduce} method.
     */
    public static final String SERVER_VERSION = "SERVER_VERSION";

    /**
     * Represents the String to identify the related {@link ApplicationReducer#launchLocalServer(Store, KBState, Object) method.}
     */
    public static final String LAUNCH_LOCAL_SERVER = "LAUNCH_LOCAL_SERVER";
    
    
    /**
     * Constructs a new ApplicationReducer and lets it register itself.
     * @see Reducer#registerReducers
     */
    public ApplicationReducer() {
        registerReducers(this);
    }

    /**
     * Represents the reducer to set the preferred name of the user.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param unused2 an unused object.
     *
     * @return the deferredState.
     */
    @Reduce(action = EXIT_APPLICATION)
    public DeferredState onExitApplication(Store<KBState> unused, KBState oldState, Object unused2) {
        System.out.println("Exiting application!");
        ClientSelector selector = oldState.selector();
        if (selector != null && selector.isRunning()) selector.stop();

        Thread selectorThread = oldState.selectorThread();
        if (selectorThread != null && selectorThread.isAlive()) selectorThread.interrupt();

        // Return old state, so that no other subscribers are called.
        return new DeferredState(oldState);
    }

    /**
     * Represents the reducer to connect the main mainClient to a server.
     *
     * @param store the store.
     * @param oldState the old state.
     * @param address the IP and port of the server.
     *
     * @return the deferredState.
     */
    @Reduce(action = CONNECT)
    public DeferredState onConnect(Store<KBState> store, KBState oldState, InetSocketAddress address) {
        DeferredState state = new DeferredState(oldState);

        List<Client> clients = new ArrayList<>();

        try {
            for(String unused: oldState.clientPreferredNames()) {
                Client client = oldState.selector().connect(address);
                clients.add(client);
            }
        } catch (IOException exc) {
            state.setFailedToConnect(true);
            return state;
        }


        Client mainClient = clients.get(0);
        state.setServerAddress(address);

        Thread selectorThread = oldState.selectorThread();
        if (selectorThread == null || !selectorThread.isAlive()) {
            assert !oldState.selector().isRunning();

            selectorThread = new Thread(oldState.selector());
            selectorThread.setName("SelectorThread");
            selectorThread.start();

            state.setSelectorThread(selectorThread);
        }

        mainClient.onVersionReply.subscribe(m -> store.dispatch(SERVER_VERSION, m));
        mainClient.onLoggedIn.subscribe(m -> store.dispatch(LOGIN, m));
        mainClient.onClientsReply.subscribe(m -> m.clients().forEach(c -> store.dispatch(ADD_CLIENT, c)));
        mainClient.onClientJoined.subscribe(m -> store.dispatch(ADD_CLIENT, m.clientData()));
        mainClient.onClientLeft.subscribe(m -> store.dispatch(REMOVE_CLIENT, m.clientData()));
        mainClient.onQuadrantReply.subscribe(m -> store.dispatchOld(new QuadrantAddAction(m)));
        mainClient.onPlayerJoined.subscribe(m -> store.dispatchOld(new PlayerAddAction(m)));
        mainClient.onPlayerLeft.subscribe(m -> store.dispatchOld(new PlayerRemoveAction(m)));
        mainClient.onGameHosted.subscribe(m -> store.dispatch(GameReducer.ADD_GAME, m.gameData()));
        mainClient.onPlayersReply.subscribe(m -> store.dispatch(GameReducer.SET_PLAYERS, m));
        mainClient.onMyGameReply.subscribe(m -> store.dispatch(GameReducer.MY_GAME, m));
        mainClient.onWinCondition.subscribe(m -> store.dispatch(GameReducer.SET_WIN_CONDITION, m));
        mainClient.onTurnStart.subscribe(m -> store.dispatch(GameReducer.START_TURN, m));
        mainClient.onTerrainTypeOfTurn.subscribe(m -> store.dispatch(GameReducer.TERRAIN_OF_TURN, m));
        mainClient.onTokenReceived.subscribe(m -> store.dispatch(GameReducer.GRANT_TOKEN, m));
        mainClient.onTokenLost.subscribe(m -> store.dispatch(GameReducer.REVOKE_TOKEN, m));
        mainClient.onScores.subscribe(m -> store.dispatch(GameReducer.SCORE, m));
        mainClient.onPlayersOfGameReply.subscribe(m -> store.dispatch(GameReducer.PLAYERS_OF_GAME, m));
        mainClient.onNamespaceLoaded.subscribe(m -> store.dispatch(NAMESPACE_LOADED, null));
        mainClient.onKicked.subscribe(m -> store.dispatch(DISCONNECT, Boolean.TRUE));
        // root stuff
        mainClient.onWrongPassword.subscribe(m -> store.dispatch(RootReducer.WRONG_PASSWORD, null));
        mainClient.onYouAreRoot.subscribe(m -> store.dispatch(RootReducer.ON_ROOT, null));


        mainClient.onGamesReply.subscribe(m -> {
            for (GameData g : m.games()) {
                store.dispatch(
                        GameReducer.ADD_GAME,
                        new GameData(g.clientId(), g.gameType(), g.gameId(), g.gameName(),
                                g.gameDescription(), g.playerLimit(), g.playersJoined())
                );
            }
        });

        mainClient.onQuadrantsReply.subscribe(m -> {
            for (int quadrantId : m.quadrantIds()) {
                mainClient.quadrantRequest(quadrantId);
            }
        });

        mainClient.onWelcomeToGame.subscribe(m -> {
            mainClient.myGameRequest();
            mainClient.playersRequest();
        });

        mainClient.onSettlementPlaced.subscribe(m -> store.dispatch(
                GameReducer.SERVER_TURN,
                new ServerTurn(m.clientId(), ServerTurn.TurnType.PLACE, m.row(), m.column(), -1, -1))
        );

        mainClient.onSettlementRemoved.subscribe(m -> store.dispatch(
                GameReducer.SERVER_TURN,
                new ServerTurn(m.clientId(), ServerTurn.TurnType.REMOVE, m.row(), m.column(), -1, -1))
        );

        mainClient.onTokenUsed.subscribe(m -> {
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

        for(int it = 0; it < oldState.clientPreferredNames().size(); ++it) {
            Client client = clients.get(it);
            String name = oldState.clientPreferredNames().get(it);
            client.login(name);
            client.onLoggedIn.subscribe(m -> store.dispatch(LOGIN, m));
        }

        mainClient.onQuadrantUploaded.subscribe(m -> {
            store.dispatch(NEW_QUADRANT_UPLOADED, m);
            mainClient.quadrantRequest(m.quadrantId());
        });

        mainClient.onMessageReceived.subscribe(m -> store.dispatch(ChatReducer.RECEIVE_MESSAGE, m));
        state.setMainClient(mainClient);
        state.setIsConnecting(true);

        clients.remove(0);
        state.setHotSeatClients(clients);

        return state;
    }

    /**
     * Represents the reducer to disconnect the main mainClient from the server they're currently connected to.
     *
     * @param store the store.
     * @param oldState the old state.
     * @param wasKicked whether the mainClient was kicked from the server.
     *
     * @return the deferredState.
     */
    @Reduce(action = DISCONNECT)
    public DeferredState onDisconnect(Store<KBState> store, KBState oldState, Boolean wasKicked) {
        DeferredState state = new DeferredState(oldState);

        oldState.sceneLoader().showMenuView();

        oldState.mainClient().disconnect();
        state.setMainClient(null);
        state.setIsConnected(false);
        state.setClientState(KBState.ClientState.NO_ROOT);

        oldState.clients().clear();
        state.setClients(oldState.clients());

        oldState.games().clear();
        state.setGames(oldState.games());

        if (wasKicked) {
            Util.showLocalizedPopupMessage("kicked", (Stage) oldState.sceneLoader().getScene().getWindow());
            if (oldState.joinedGame()) {
                oldState.Bots().keySet().forEach(c -> store.dispatch(BotReducer.DISCONNECT_BOT, c));
                state.setPlayers(null);
                state.setScores(null);
                state.setToken(null);
                state.setGameLastTurn(null);
                state.setNextTerrainCard(null);
                state.setNextPlayer(-1);
                state.setGameStarted(false);
                state.setGameMap(null);
                state.setMyGameReply(null);
                state.setPlayersMap(null);
                state.setCurrentPlayer(null);
                state.setJoinedGame(false);
                state.setWinConditions(new ArrayList<>());
            }
        }

        return state;
    }

    /**
     * Represents the reducer to log in to the server after a connection has been established.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param client the client that successfully logged in.
     *
     * @return the deferredState.
     */
    @Reduce(action = LOGIN)
    public DeferredState onLogin(Store<KBState> unused, KBState oldState, Client client) {
        DeferredState state = new DeferredState(oldState);

        boolean hotSeatClientsLoggedIn = oldState.hotSeatClients().isEmpty()
                || oldState
            .hotSeatClients()
            .stream()
            .allMatch(Client::isLoggedIn);

        if(oldState.mainClient().isConnected() && hotSeatClientsLoggedIn) {
            state.setIsConnecting(false);
            state.setIsConnected(true);
            System.out.println("Is connected.");
        }

        boolean isMainClient = oldState.mainClient() == client;

        if(isMainClient)
            client.serverVersion();

        client.loadNamespace();

        if(isMainClient) {
            client.clientsRequest();
            client.gamesRequest();
        }

        return state;
    }

    /**
     * Represents the reducer to add a mainClient to the state's list of clients.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the data object of the mainClient to be added.
     *
     * @return the deferredState.
     */
    @Reduce(action = ADD_CLIENT)
    public DeferredState onAddClient(Store<KBState> unused, KBState oldState, ClientData payload) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        clients.put(payload.clientId(), payload);
        state.setClients(clients);

        return state;
    }

    /**
     * Represents the reducer to remove a mainClient from the state's list of clients.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the data object of the mainClient to be removed.
     *
     * @return the deferredState.
     */
    @Reduce(action = REMOVE_CLIENT)
    public DeferredState onRemoveClient(Store<KBState> unused, KBState oldState, ClientData payload) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients();
        clients.remove(payload.clientId());
        state.setClients(clients);

        return state;
    }

    /**
     * Represents the reducer to set the SceneLoader in the state.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param sceneLoader the SceneLoader object to be set.
     *
     * @return the deferredState.
     */
    @Reduce(action = SET_SCENELOADER)
    public DeferredState onSetSceneLoader(Store<KBState> unused, KBState oldState, SceneLoader sceneLoader) {
        DeferredState state = new DeferredState(oldState);
        state.setSceneLoader(sceneLoader);
        return state;
    }

    /**
     * Represents the reducer to handle the confirmation of a newly uploaded quadrant and its uploader.
     *
     * @param unused the store.
     * @param oldState the old state.
     * @param payload the data object containing the ID of the newly uploaded quadrant and the ID of the uploader.
     *
     * @return the deferredState.
     */
    @Reduce(action = NEW_QUADRANT_UPLOADED)
    public DeferredState onNewQuadrantUploaded(Store<KBState> unused, KBState oldState, QuadrantUploaded payload) {
        DeferredState state = new DeferredState(oldState);
        state.setQuadrantUploaded(payload);
        return state;
    }

    /**
     * Represents the reducer to set the version of the server in the state.
     *
     * @param unused   the store.
     * @param oldState the old state.
     * @param version  the VersionReply with the server version.
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
     * @param unused   the store.
     * @param oldState the old state.
     * @param unused2  an unused object.
     * @return the deferredState.
     */
    @Reduce(action = NAMESPACE_LOADED)
    public DeferredState onNameSpaceLoaded(Store<KBState> unused, KBState oldState, Object unused2) {
        //This prevents the bug, that the server processes the '?quadrants' message before
        // it starts to load the namespace.
        oldState.mainClient().quadrantsRequest();
        return new DeferredState(oldState);
    }

    /**
     * Launches the embedded server as a local process, unless an instance is already running.
     * @param unused the state store; not used by this reducer.
     * @param oldState the old state prior invoking this reducer.
     * @param unused2 the payload for the reducer; not used by this reducer.
     * @return an instance of {@link DeferredState} containing a set of "transactions" issued by the reducers.
     */
    @Reduce(action = LAUNCH_LOCAL_SERVER)
    public DeferredState launchLocalServer(Store<KBState> unused, KBState oldState, Object unused2) {
        DeferredState state = new DeferredState(oldState);

        if(oldState.server() != null)
            return state;

        Process process;
        try {
            process = Server.launch();
            System.out.println("Launched local server!");
        } catch(IOException exc) {
            System.out.println("Failed to launch local server!");
            return state;
        }

        state.setServer(process);

        return state;
    }


}
