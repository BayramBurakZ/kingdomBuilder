package kingdomBuilder;

import javafx.application.Application;

import java.io.IOException;

import kingdomBuilder.gui.KingdomBuilderApplication;
import kingdomBuilder.reducers.KBReducer;
import kingdomBuilder.redux.Store;

public class Boot {

    public static void main(String[] args) throws IOException {

        // TODO: maybe move construction to Store class or replace with factory or enum singleton or whatever
        // this way the construction is visible in the Boot class
        // can also construct an object without setting it as the singleton, may or may not be desired
        Store<KBState> store = new Store<>(new KBState(), new KBReducer());
        Store.setInstance(store);

        /*
        var address = "juliankirsch.me";

        // TODO: handle no connection; threads still created
        Client client = new Client(address, 6666);
        Client.setMain(client); // TODO: same as with Store class

        // display "Connecting..." and grey out GUI elements until this variable contains a response
        // maybe eventually timeout and ask to retry connecting
        // therefore somehow pass this variable to GUI for handling after launch()
        var welcomeFut = client.join("Erik");

        client.onClientJoined.subscribe(c -> {
            store.dispatch(new ClientAddAction(c));
        });

        client.onClientLeft.subscribe(c -> {
            store.dispatch(new ClientRemoveAction(c));
        });

        // start listening to server with main client
        Thread clientThread = new Thread(client::listen);
        clientThread.start();

        // wait for the WelcomeToServer message before proceeding
        try {
            var welcomeToServer = welcomeFut.get();
            store.dispatch(new SetClientIDAction(welcomeToServer.clientId()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Main Client ID: " + store.getState().clientID);

        // wait to receive all clients from server
        var clientsFut = client.requestClients();
        try {
            var clients = clientsFut.get().clients();
            System.out.println(clients);
            if (clients != null) {
                for (var c : clients) {
                    store.dispatch(new ClientAddAction(c));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        */
        Application.launch(KingdomBuilderApplication.class);
    }
}
