package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gamelogic.Game.WinCondition;
import kingdomBuilder.gamelogic.GameInfo;
import kingdomBuilder.gamelogic.MapReadOnly;
import kingdomBuilder.gamelogic.Player;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.network.protocol.PlayerData;
import kingdomBuilder.redux.Action;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import kingdomBuilder.generated.DeferredState;

import java.io.IOException;
import java.util.LinkedHashSet;

public class KBReducer implements Reducer<KBState> {

    @Override
    public DeferredState reduce(Store<KBState> store, KBState oldState, Action action) {
        System.out.println("Reducer Log: " + action.getClass().getSimpleName());

        /*
        https://stackoverflow.com/questions/29570767/switch-over-type-in-java
        using HashMap to accomplish the same thing at runtime that a switch statement would do at compile-time
         */

        if (action instanceof ClientAddAction a)
            return reduce(oldState, a);
        else if (action instanceof ClientRemoveAction a)
          return reduce(oldState, a);
        else if (action instanceof SetPreferredNameAction a)
          return reduce(oldState, a);
        else if (action instanceof ChatSendAction a)
          return reduce(oldState, a);
        else if (action instanceof ChatReceiveAction a)
          return reduce(oldState, a);
        else if (action instanceof ConnectAction a)
          return reduce(store, oldState, a);
        else if (action instanceof DisconnectAction a)
          return reduce(oldState, a);
        else if (action instanceof SetSceneLoaderAction a)
          return reduce(oldState, a);
        else if (action instanceof BetterColorModeAction a)
            return reduce(oldState, a);
        else if (action instanceof LoggedInAction a)
            return reduce(oldState, a);
        else if (action instanceof ApplicationExitAction a)
            return reduce(oldState, a);
        else if (action instanceof HostGameAction a)
            return reduce(oldState, a);
        else if (action instanceof GameAddAction a)
            return reduce(oldState, a);
        else if (action instanceof QuadrantAddAction a)
            return reduce(oldState, a);
        else if (action instanceof JoinGameAction a)
            return reduce(oldState, a);
        else if (action instanceof PlayerAddAction a)
            return reduce(oldState, a);
        else if (action instanceof PlayerRemoveAction a)
            return reduce(oldState, a);
        else if (action instanceof SetPlayersAction a)
            return reduce(oldState, a);
        else if (action instanceof MyGameAction a)
            return reduce(oldState, a);
        else if (action instanceof WinConditionsAction a)
            return reduce(oldState, a);
        else if (action instanceof TerrainOfTurnAction a)
            return reduce(oldState, a);

        return new DeferredState(oldState);
    }

    private DeferredState reduce(KBState oldState, ClientAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        clients.put(a.clientData.clientId(), a.clientData);
        state.setClients(clients);

        return state;
    }

    private DeferredState reduce(KBState oldState, ClientRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        clients.remove(a.clientData.clientId());
        state.setClients(clients);

        return state;
    }

    private DeferredState reduce(KBState oldState, SetPreferredNameAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setClientPreferredName(a.clientName);
        return state;
    }

    private DeferredState reduce(KBState oldState, ChatSendAction a) {
        oldState.client.chat(a.receiverIds, a.message);
        return new DeferredState(oldState);
    }

    // TODO: remove sceneloader/controller
    private DeferredState reduce(KBState oldState, ChatReceiveAction a) {
        var sceneLoader = oldState.sceneLoader;
        var chatViewController = sceneLoader.getChatViewController();
        chatViewController.onMessage(a.chatMessage);
        return new DeferredState(oldState);
    }

    private DeferredState reduce(Store<KBState> store, KBState oldState, ConnectAction a) {
        DeferredState state = new DeferredState(oldState);

        Client client;
        try { client = oldState.selector.connect(a.address); }
        catch(IOException exc) {
            state.setFailedToConnect(true);
            return state;
        }

        Thread selectorThread = oldState.selectorThread;
        if(selectorThread == null || !selectorThread.isAlive()) {
            assert !oldState.selector.isRunning();

            selectorThread = new Thread(oldState.selector);
            selectorThread.setName("SelectorThread");
            selectorThread.start();

            state.setSelectorThread(selectorThread);
        }

        client.onLoggedIn.subscribe(m -> store.dispatch(new LoggedInAction(m)));

        client.onClientsReply.subscribe(m -> {
            for (ClientData c : m.clients()) {
                store.dispatch( // TODO: use ClientData in network message
                        new ClientAddAction(new ClientData(c.clientId(), c.name(), c.gameId())));
            }
        });

        client.onGamesReply.subscribe(m -> {
            for (GameData g : m.games()) {
                store.dispatch( // TODO: use GameData in network message
                        new GameAddAction(new GameData(g.clientId(), g.gameType(), g.gameId(), g.gameName(),
                                g.gameDescription(), g.playerLimit(), g.playersJoined())));
            }
        });

        client.onQuadrantsReply.subscribe(m -> {
            for (int quadrantId : m.quadrantIds()) {
                client.quadrantRequest(quadrantId);
            }
        });

        client.onMessageReceived.subscribe(m -> store.dispatch(new ChatReceiveAction(m)));

        client.onQuadrantReply.subscribe(m -> store.dispatch(new QuadrantAddAction(m)));

        client.onClientJoined.subscribe(m -> store.dispatch(new ClientAddAction(m.clientData())));

        client.onClientLeft.subscribe(m -> store.dispatch(new ClientRemoveAction(m.clientData())));

        client.onMessageReceived.subscribe(m -> store.dispatch(new ChatReceiveAction(m)));

        client.onPlayerJoined.subscribe(m -> store.dispatch(new PlayerAddAction(m)));

        client.onPlayerLeft.subscribe(m -> store.dispatch(new PlayerRemoveAction(m)));

        client.onGameHosted.subscribe(m -> store.dispatch(new GameAddAction(m.gameData())));

        client.onWelcomeToGame.subscribe(m -> {
            client.myGameRequest();
            client.playersRequest();
        });

        client.onPlayersReply.subscribe(m -> store.dispatch(new SetPlayersAction(m)));

        client.onMyGameReply.subscribe(m -> store.dispatch(new MyGameAction(m)));

        client.login(oldState.clientPreferredName);

        client.onWinCondition.subscribe(m -> store.dispatch(new WinConditionsAction(m)));

        client.onTerrainTypeOfTurn.subscribe(m -> store.dispatch(new TerrainOfTurnAction(m)));

        state.setClient(client);
        state.setIsConnecting(true);

        return state;
    }

    private DeferredState reduce(KBState state, ApplicationExitAction a) {
        ClientSelector selector = state.selector;
        if(selector != null && selector.isRunning()) selector.stop();

        Thread selectorThread = state.selectorThread;
        if(selectorThread != null && selectorThread.isAlive()) selectorThread.interrupt();

        // Return old state, so that no other subscribers are called.
        return new DeferredState(state);
    }

    private DeferredState reduce(KBState oldState, LoggedInAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setIsConnecting(false);
        state.setIsConnected(true);
        System.out.println("Is connected.");

        oldState.client.loadNamespace();
        oldState.client.clientsRequest();
        // TODO: uncomment if fixed
        //oldState.client.gamesRequest();
        oldState.client.quadrantsRequest();

        return state;
    }

    // TODO: remove sceneloader/controller
    private DeferredState reduce(KBState oldState, DisconnectAction a) {
        DeferredState state = new DeferredState(oldState);

        if(a.wasKicked) {
            var sceneLoader = oldState.sceneLoader;
            sceneLoader.getChatViewController().onYouHaveBeenKicked();
        }

        oldState.client.disconnect();
        state.setClient(null);
        state.setIsConnected(false);

        oldState.clients.clear();
        state.setClients(oldState.clients);

        oldState.games.clear();
        state.setGames(oldState.games);

        return state;
    }

    private DeferredState reduce(KBState oldState, SetSceneLoaderAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setSceneLoader(a.sceneLoader);
        return state;
    }

    private DeferredState reduce(KBState oldState, BetterColorModeAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setBetterColorsActive(a.active);
        return state;
    }

    private DeferredState reduce(KBState oldState, PlayerAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        var clientData = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, clientData.name(), a.gameId));
        state.setClients(clients);

        if (a.gameId == oldState.client.getGameId()) {
            // for some reason you only get the color of a player via ?players
            oldState.client.playersRequest();
        }
        return state;
    }

    private DeferredState reduce(KBState oldState, PlayerRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        var client = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, client.name(), Client.NO_ID));
        state.setClients(clients);
        return state;
    }

    private DeferredState reduce(KBState oldState, HostGameAction a) {
        DeferredState state = new DeferredState(oldState);
        oldState.client.hostGame(a.gameName, a.gameDescription, a.playerLimit, a.timeLimit, a.turnLimit,
                a.quadrantId1, a.quadrantId2, a.quadrantId3, a.quadrantId4);
        return state;
    }

    private DeferredState reduce(KBState oldState, GameAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var games = oldState.games;
        games.put(a.gameData.gameId(), a.gameData);
        state.setGames(games);
        return state;
    }

    private DeferredState reduce(KBState oldState, QuadrantAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var quadrants = oldState.quadrants;
        quadrants.put(a.quadrantId, a.fieldTypes);
        state.setQuadrants(quadrants);
        return state;
    }

    private DeferredState reduce(KBState oldState, JoinGameAction a) {
        DeferredState state = new DeferredState(oldState);
        oldState.client.joinGame(a.gameId);
        state.setClient(oldState.client);
        return state;
    }

    private DeferredState reduce(KBState oldState, SetPlayersAction a) {
        DeferredState state = new DeferredState(oldState);
        final var oldGameInfo = oldState.gameInfo;

        LinkedHashSet<PlayerData> playersOfGame = oldGameInfo.playersOfGame();
        playersOfGame.addAll(a.playersReply.playerDataList());

        GameInfo newGameInfo = new GameInfo(playersOfGame, oldGameInfo.gameInformation(),
                oldGameInfo.map(), oldGameInfo.winConditions(), oldGameInfo.terrainTypeOfTurn());
        state.setGameInfo(newGameInfo);

        if (newGameInfo.playersOfGame() != null)
            if (newGameInfo.playersOfGame().size() == newGameInfo.gameInformation().playerLimit())
                if (newGameInfo.winConditions() != null) {
                    System.out.println("Game created from PlayerAction");
                    state = createGame(oldState, state, newGameInfo);
                }

        return state;
    }

    private DeferredState reduce(KBState oldState, MyGameAction a) {
        DeferredState state = new DeferredState(oldState);
        var oldGameInfo = oldState.gameInfo;

        MapReadOnly map = new MapReadOnly(
                MapReadOnly.DEFAULT_STARTING_TOKEN_COUNT,
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId1()),
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId2()),
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId3()),
                oldState.quadrants.get(a.myGameReply.boardData().quadrantId4()));

        GameInfo newGameInfo = new GameInfo(oldGameInfo.playersOfGame(), a.myGameReply, map,
                oldState.gameInfo.winConditions(), oldGameInfo.terrainTypeOfTurn());
        state.setGameInfo(newGameInfo);
        return state;
    }

    // TODO: pls someone make it better D:
    private DeferredState reduce(KBState oldState, WinConditionsAction a) {
        DeferredState state = new DeferredState(oldState);

        // TODO: debug
        System.out.println(
                a.winConditionReply.winCondition1() + " " +
                a.winConditionReply.winCondition2() + " " +
                a.winConditionReply.winCondition3());

        WinCondition[] winConditions = {
                WinCondition.valueOf(a.winConditionReply.winCondition1()),
                WinCondition.valueOf(a.winConditionReply.winCondition2()),
                WinCondition.valueOf(a.winConditionReply.winCondition3())
        };

        final var oldGameInfo = oldState.gameInfo;
        GameInfo newGameInfo = new GameInfo(oldGameInfo.playersOfGame(), oldGameInfo.gameInformation(),
                oldGameInfo.map(), winConditions, oldGameInfo.terrainTypeOfTurn());
        state.setGameInfo(newGameInfo);

        if (newGameInfo.winConditions() != null)
            if (newGameInfo.gameInformation() != null)
                if (newGameInfo.playersOfGame().size() == newGameInfo.gameInformation().playerLimit()) {
                    System.out.println("Game created from WinConditionAction");
                    state = createGame(oldState, state, newGameInfo);
                }

        return state;
    }

    /**
     * Creates a game and sets it in the state.
     * @param oldState the oldState to read data.
     * @param state the deferredState to set the changes.
     * @param gameInfo the updated gameInfo.
     * @return the updated deferredState.
     */
    private DeferredState createGame(KBState oldState, DeferredState state, GameInfo gameInfo) {

        Player[] player = new Player[gameInfo.playersOfGame().size()];

        int i = 0;
        for (PlayerData pd : gameInfo.playersOfGame()) {
            ClientData cd = oldState.clients.get(pd.clientId());

            player[i++] = new Player(pd.clientId(), cd.name(), Game.PlayerColor.valueOf(pd.color()), 40);
        }

        state.setGame(new Game(
                oldState.gameInfo,
                player[0].ID,
                player));

        return state;

    }

    private DeferredState reduce(KBState oldState, TerrainOfTurnAction a) {
        DeferredState state = new DeferredState(oldState);
        GameInfo oldGameInfo = oldState.gameInfo;
        GameInfo newGameInfo = new GameInfo(oldGameInfo.playersOfGame(),
                oldGameInfo.gameInformation(), oldGameInfo.map(), oldGameInfo.winConditions(),
                a.terrainTypeOfTurn);
        state.setGameInfo(newGameInfo);
        return state;
    }

}
