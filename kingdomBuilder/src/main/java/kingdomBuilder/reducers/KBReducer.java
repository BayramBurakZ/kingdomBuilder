package kingdomBuilder.reducers;

import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.redux.Action;
import kingdomBuilder.redux.Reducer;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KBReducer implements Reducer<KBState> {

    @Override
    public KBState reduce(KBState oldState, Action action) {
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
            return reduce(oldState, a);
        } else if (action instanceof DisconnectAction a) {
            return reduce(oldState, a);
        } else if (action instanceof SetMainControllerAction a) {
            return reduce(oldState, a);
        }

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

    private KBState reduce(KBState oldState, ChatSendAction a) {
        oldState.client.chat(a.message, a.receiverIds);
        return oldState;
    }

    private KBState reduce(KBState oldState, ChatReceiveAction a) {
        var sceneLoader = oldState.controller.getSceneLoader();
        var chatViewController = sceneLoader.getChatViewController();
        chatViewController.onMessage(a.chatMessage);
        return oldState;
    }

    private KBState reduce(KBState oldState, ConnectAction a) {
        Client client;
        try {
            client = new Client(a.address, a.port);
        } catch (IOException e) {
            //TODO: maybe a popup
            System.out.println("Address not found");
            KBState state = new KBState(oldState);
            state.failedToConnect = true;
            return state;
        }

        // create new state after client creation in case client connection fails
        KBState state = new KBState(oldState);

        // Client is connected
        state.isConnected = true;

        // Reset failedToConnect
        state.failedToConnect = false;

        // start listening to server with main client
        Thread clientThread = new Thread(client::listen, "Main-Client");
        clientThread.start();
        state.clientThread = clientThread;

        var welcomeFut = client.join(state.clientPreferredName);

        // wait for the WelcomeToServer message before proceeding
        try {
            welcomeFut.get(1000, TimeUnit.MILLISECONDS);
            state.client = client;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return oldState;
        }

        Store<KBState> store = Store.get();

        client.onClientJoined.subscribe(c -> store.dispatch(new ClientAddAction(c)));

        client.onClientLeft.subscribe(c -> store.dispatch(new ClientRemoveAction(c)));

        client.onMessage.subscribe(m -> store.dispatch(new ChatReceiveAction(m)));

        client.onYouHaveBeenKicked.subscribe(m -> store.dispatch(new DisconnectAction(true)));

        System.out.println("Main Client ID: " + client.getId());

        // wait to receive all clients from server
        var clientsFut = client.requestClients();
        try {
            var clients = clientsFut.get(1000, TimeUnit.MILLISECONDS).clients();
            if (clients != null) {
                for (var c : clients) {
                    // add without an action to avoid many notifications when joining
                    state.clients.put(c.clientId(), new ClientDAO(c.clientId(), c.name(), c.gameId()));
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return oldState;
        }
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
