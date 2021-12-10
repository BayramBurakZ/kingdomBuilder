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

public class KBReducer implements Reducer<KBState> {

    @Override
    public KBState reduce(KBState oldState, Action action) {
        System.out.println("Reducer Log: " + action.getClass().getSimpleName());
        
        if (action instanceof ClientAddAction a) {
            KBState state = new KBState(oldState);
            state.clients.put(a.id, new ClientDAO(a.id, a.name, a.gameId));

            var sceneLoader = oldState.controller.getSceneLoader();
            sceneLoader.getChatViewController().onClientJoined(a.id, a.name, a.gameId);

            return state;
        }

        if (action instanceof ClientRemoveAction a) {
            KBState state = new KBState(oldState);
            state.clients.remove(a.id);

            var sceneLoader = oldState.controller.getSceneLoader();
            sceneLoader.getChatViewController().onClientLeft(a.id, a.name, a.gameId);

            return state;
        }

        if (action instanceof SetClientNameAction a) {
            KBState state = new KBState(oldState);
            state.clientPreferredName = a.clientName;
            return state;
        }

        if (action instanceof ChatSendAction a) {
            oldState.client.chat(a.message, a.receiverIds);
            return oldState;
        }

        if (action instanceof ChatReceiveAction a) {
            var sceneLoader = oldState.controller.getSceneLoader();
            var chatViewController = sceneLoader.getChatViewController();
            chatViewController.onMessage(a.chatMessage);
            return oldState;
        }

        if (action instanceof ClientConnectAction a) {
            KBState state = oldState;
            try {
                Client client = new Client(a.address, a.port);

                // create new state after client creation in case client connection fails
                state = new KBState(oldState);

                // start listening to server with main client
                Thread clientThread = new Thread(client::listen);
                clientThread.start();

                var welcomeFut = client.join(state.clientPreferredName);

                // wait for the WelcomeToServer message before proceeding
                try {
                    var welcomeToServer = welcomeFut.get();
                    state.client = client;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return oldState;
                }

                Store<KBState> store = Store.get();

                client.onClientJoined.subscribe(c -> {
                    store.dispatch(new ClientAddAction(c));
                });

                client.onClientLeft.subscribe(c -> {
                    store.dispatch(new ClientRemoveAction(c));
                });

                client.onMessage.subscribe(m -> {
                    store.dispatch(new ChatReceiveAction(m));
                });

                System.out.println("Main Client ID: " + client.getId());

                // wait to receive all clients from server
                var clientsFut = client.requestClients();
                try {
                    var clients = clientsFut.get().clients();
                    if (clients != null) {
                        for (var c : clients) {
                            // add without an action to avoid many notifications when joining
                            state.clients.put(c.clientId(), new ClientDAO(c.clientId(), c.name(), c.gameId()));
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                var sceneLoader = oldState.controller.getSceneLoader();
                sceneLoader.getChatViewController().onWelcomeToServer();

            } catch (IOException e) {
                //TODO: maybe a popup
                System.out.println("Address not found");
                return oldState;
            }
            return state;
        }

        if (action instanceof ClientDisconnectAction a) {
            oldState.client.disconnect();
            KBState state = new KBState(oldState);
            state.clients.clear();
            state.games.clear();
            return state;
        }

        if (action instanceof SetMainControllerAction a) {
            KBState state = new KBState(oldState);
            state.controller = a.controller;
            return state;
        }

        return oldState;
    }
}
