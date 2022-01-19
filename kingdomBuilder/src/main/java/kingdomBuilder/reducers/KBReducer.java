package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
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

        if (action instanceof ClientAddAction a) {
            return reduce(oldState, a);
        } else if (action instanceof ClientRemoveAction a) {
            return reduce(oldState, a);
        } else if (action instanceof SetPreferredNameAction a) {
            return reduce(oldState, a);
        } else if (action instanceof ChatSendAction a) {
            return reduce(oldState, a);
        } else if (action instanceof ChatReceiveAction a) {
            return reduce(oldState, a);
        } else if (action instanceof ConnectAction a) {
            return reduce(store, oldState, a);
        } else if (action instanceof DisconnectAction a) {
            return reduce(oldState, a);
        } else if (action instanceof SetMainControllerAction a) {
            return reduce(oldState, a);
        } else if (action instanceof LoggedInAction a)
            return reduce(oldState, a);
        else if(action instanceof ApplicationExitAction a)
            return reduce(oldState, a);

        return new DeferredState(oldState);
    }

    private DeferredState reduce(KBState oldState, ClientAddAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        clients.put(a.id, new ClientDAO(a.id, a.name, a.gameId));
        state.setClients(clients);

        var sceneLoader = oldState.controller.getSceneLoader();
        sceneLoader.getChatViewController().onClientJoined(a.id, a.name, a.gameId);

        return state;
    }

    private DeferredState reduce(KBState oldState, ClientRemoveAction a) {
        DeferredState state = new DeferredState(oldState);
        final var clients = oldState.clients;
        clients.remove(a.id);
        state.setClients(clients);

        var sceneLoader = oldState.controller.getSceneLoader();
        sceneLoader.getChatViewController().onClientLeft(a.id, a.name, a.gameId);

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

    private DeferredState reduce(KBState oldState, ChatReceiveAction a) {
        var sceneLoader = oldState.controller.getSceneLoader();
        var chatViewController = sceneLoader.getChatViewController();
        // chatViewController.onMessage(a.chatMessage);
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

        client.onLoggedIn.subscribe(c -> store.dispatch(new LoggedInAction(c)));
        client.onClientJoined.subscribe(c -> store.dispatch(new ClientAddAction(c.clientId(), c.name(), c.gameId())));
        client.onClientLeft.subscribe(c -> store.dispatch(new ClientRemoveAction(c.clientId(), c.name(), c.gameId())));
        client.onMessageReceived.subscribe(m -> store.dispatch(new ChatReceiveAction(m)));

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
        return state;
    }

    private DeferredState reduce(KBState oldState, DisconnectAction a) {
        DeferredState state = new DeferredState(oldState);

        if(a.wasKicked) {
            var sceneLoader = oldState.controller.getSceneLoader();
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

    private DeferredState reduce(KBState oldState, SetMainControllerAction a) {
        DeferredState state = new DeferredState(oldState);
        state.setController(a.controller);
        return state;
    }
}
