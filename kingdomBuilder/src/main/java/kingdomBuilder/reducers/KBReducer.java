package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.redux.Action;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import java.io.IOException;

public class KBReducer implements Reducer<KBState> {

    @Override
    public KBState reduce(Store<KBState> store, KBState oldState, Action action) {
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

        return oldState;
    }

    private KBState reduce(KBState oldState, ClientAddAction a) {
        KBState state = new KBState(oldState);
        state.clients.put(a.id, new ClientDAO(a.id, a.name, a.gameId));

        var sceneLoader = oldState.controller.getSceneLoader();
        sceneLoader.getChatViewController().onClientJoined(a.id, a.name, a.gameId);

        return state;
    }

    private KBState reduce(KBState oldState, ClientRemoveAction a) {
        KBState state = new KBState(oldState);
        state.clients.remove(a.id);

        var sceneLoader = oldState.controller.getSceneLoader();
        sceneLoader.getChatViewController().onClientLeft(a.id, a.name, a.gameId);

        return state;
    }

    private KBState reduce(KBState oldState, SetPreferredNameAction a) {
        KBState state = new KBState(oldState);
        state.clientPreferredName = a.clientName;
        return state;
    }

    private KBState reduce(KBState state, ChatSendAction a) {
        state.client.chat(a.receiverIds, a.message);
        return state;
    }

    private KBState reduce(KBState oldState, ChatReceiveAction a) {
        var sceneLoader = oldState.controller.getSceneLoader();
        var chatViewController = sceneLoader.getChatViewController();
        // chatViewController.onMessage(a.chatMessage);
        return oldState;
    }

    private KBState reduce(Store<KBState> store, KBState oldState, ConnectAction a) {
        Client client;
        try { client = oldState.selector.connect(a.address); }
        catch(IOException exc) {
            System.out.println("Failed to connect to server.");

            final KBState state = new KBState(oldState);
            state.failedToConnect = true;
            return state;
        }

        Thread selectorThread = oldState.selectorThread;
        if(selectorThread == null || !selectorThread.isAlive()) {
            assert !oldState.selector.isRunning();
            selectorThread = new Thread(oldState.selector);
            selectorThread.start();

            System.out.println("Started selector thread!");
        }

        client.onLoggedIn.subscribe(c -> store.dispatch(new LoggedInAction(c)));
        client.onClientJoined.subscribe(c -> store.dispatch(new ClientAddAction(c.clientId(), c.name(), c.gameId())));
        client.onClientLeft.subscribe(c -> store.dispatch(new ClientRemoveAction(c.clientId(), c.name(), c.gameId())));
        client.onMessageReceived.subscribe(m -> store.dispatch(new ChatReceiveAction(m)));

        client.login(oldState.clientPreferredName);

        KBState state = new KBState(oldState);
        state.client = client;
        state.isConnecting = true;
        state.selectorThread = selectorThread;

        return state;
    }

    private KBState reduce(KBState oldState, ApplicationExitAction a) {
        ClientSelector selector = oldState.selector;
        if(selector != null && selector.isRunning())
            selector.stop();

        Thread selectorThread = oldState.selectorThread;
        if(selectorThread != null && selectorThread.isAlive())
            selectorThread.interrupt();

        // Return old state, so that no other subscribers are called.
        return oldState;
    }

    private KBState reduce(KBState oldState, LoggedInAction a) {
        System.out.println("Logged in!");
        KBState state = new KBState(oldState);
        state.isConnecting = false;
        return state;
    }

    private KBState reduce(KBState oldState, DisconnectAction a) {
        KBState state = new KBState(oldState);
        // TODO: kicked action ?
        if (a.wasKicked) {
            var sceneLoader = oldState.controller.getSceneLoader();
            sceneLoader.getChatViewController().onYouHaveBeenKicked();
        }
        state.client.disconnect();
        state.clients.clear();
        state.games.clear();
        state.client = null;
        state.isConnected = false;
        return state;
    }

    private KBState reduce(KBState oldState, SetMainControllerAction a) {
        KBState state = new KBState(oldState);
        state.controller = a.controller;
        return state;
    }
}
