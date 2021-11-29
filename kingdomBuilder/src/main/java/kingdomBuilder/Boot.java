package kingdomBuilder;

import kingdomBuilder.actions.IncrementAction;
import kingdomBuilder.network.Client;
import java.io.IOException;

import kingdomBuilder.reducers.SampleReducer;
import kingdomBuilder.redux.Store;

public class Boot {

    public static void main(String[] args) throws IOException {
            Store<SampleState> store = new Store<>(new SampleState(0), new SampleReducer());
            store.subscribe(sampleState -> { System.out.println("Counter is now: " + sampleState.getCounter()); });
            store.dispatch(new IncrementAction());

            Client client = new Client("localhost", 6666);
            var fut = client.join("Yeet42");
    }
}