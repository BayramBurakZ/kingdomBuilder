package kingdomBuilder;

import javafx.application.Application;

import java.io.IOException;

import kingdomBuilder.actions.ClientAddAction;
import kingdomBuilder.actions.IncrementAction;
import kingdomBuilder.gui.KingdomBuilderApplication;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.ClientJoined;
import kingdomBuilder.reducers.KBReducer;
import kingdomBuilder.reducers.SampleReducer;
import kingdomBuilder.redux.Store;

public class Boot {

    public static void main(String[] args) throws IOException {

        /*
        Store<SampleState> store = new Store<>(new SampleState(0), new SampleReducer());
        store.subscribe(sampleState -> {
            System.out.println("Counter is now: " + sampleState.getCounter());
        });
        store.dispatch(new IncrementAction());
         */

        // TODO: maybe move construction to Store class or replace with factory or enum singleton or whatever
        // this way the construction is visible in the Boot class
        // can also construct an object without setting it as the singleton, may or may not be desired
        Store<KBState> store = new Store<>(new KBState(), new KBReducer());
        Store.setInstance(store);

        // TODO: handle no connection; threads still created
        Client client = new Client("localhost", 6666);
        Client.setMain(client); // TODO: same as with Store class

        // display "Connecting..." and grey out GUI elements until this variable contains a response
        // maybe eventually timeout and ask to retry connecting
        // therefore somehow pass this variable to GUI for handling after launch()
        var fut = client.join("Yeet42");

        client.onClientJoined.subscribe(c -> {
            store.dispatch(new ClientAddAction(c));
        });

        Thread clientThread = new Thread(client::listen);
        clientThread.start();

        Client testClient1 = new Client("localhost", 6666);
        testClient1.join("TestClient1");
        Client testClient2 = new Client("localhost", 6666);
        testClient2.join("TestClient2");

        Application.launch(KingdomBuilderApplication.class);
    }
}
