package kingdomBuilder;


import kingdomBuilder.redux.Store;

public class Boot {

    public static void main(String[] args) {
        Store<SampleState> store = new Store<>(new SampleState(0), new SampleReducer());
        store.subscribe(sampleState -> { System.out.println("Counter is now: " + sampleState.getCounter()); });
        store.dispatch(new IncrementAction());
    }

}