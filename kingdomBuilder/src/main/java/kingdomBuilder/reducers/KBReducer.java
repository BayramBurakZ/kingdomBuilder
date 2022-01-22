package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.network.protocol.ClientData;
import kingdomBuilder.network.protocol.GameData;
import kingdomBuilder.redux.Action;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import kingdomBuilder.generated.DeferredState;

import java.io.IOException;

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
        else if (action instanceof SetQuadrantIDsAction a)
            return reduce(oldState, a);

        return new DeferredState(oldState);
    }

    // TODO: remove sceneloader/controller
    private DeferredState reduce(KBState oldState, ClientAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        clients.put(a.clientData.clientId(), a.clientData);
        state.setClients(clients);

        var sceneLoader = oldState.sceneLoader;
        sceneLoader.getChatViewController().onClientJoined(a.clientData);

        return state;
    }

    // TODO: remove sceneloader/controller
    private DeferredState reduce(KBState oldState, ClientRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        clients.remove(a.clientData.clientId());
        state.setClients(clients);

        var sceneLoader = oldState.sceneLoader;
        sceneLoader.getChatViewController().onClientLeft(a.clientData);

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

        //client.onWelcomeToGame.subscribe();

        client.onBoardReply.subscribe(m -> store.dispatch(new SetQuadrantIDsAction(m.boardData())));

        client.login(oldState.clientPreferredName);

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

        /* TODO:
            load kingdom_builder
        */
        oldState.client.loadNamespace();
        oldState.client.clientsRequest();
        //oldState.client.gamesRequest();
        oldState.client.quadrantsRequest();

        return state;
    }

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
        var client = clients.get(a.clientId);
        clients.put(a.clientId, new ClientData(a.clientId, client.name(), a.gameId));
        state.setClients(clients);
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

    private DeferredState reduce(KBState oldState, SetQuadrantIDsAction a) {
        DeferredState state = new DeferredState(oldState);
        // TODO: maybe just use BoardData in Game, this isn't MVC
        oldState.gameQuadrantIDs = new Game.QuadrantIDs(
                a.boardData.quadrantId1(),
                a.boardData.quadrantId2(),
                a.boardData.quadrantId3(),
                a.boardData.quadrantId4());
        return state;
    }
/*
    private DeferredState reduce(KBState oldState, StartGameAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setGame(new Game(
                a.gameName,
                a.gameDescription,
                a.playerLimit,
                a.timeLimit,
                a.turnLimit,
                new Game.QuadrantIDs(a.quadrantId1, a.quadrantId2, a.quadrantId3, a.quadrantId4),
                a.
        ));
        return state;
    }
*/
}
