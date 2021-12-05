package kingdomBuilder.redux;

import kingdomBuilder.KBState;

import java.util.ArrayList;
import java.util.List;

public class Store<State> {
    private static Store<?> instance;

    private State state;
    private final Reducer<State> reducer;
    private List<Subscriber<State>> subscribers;

    public Store(State state, Reducer<State> reducer) {
        this.state = state;
        this.reducer = reducer;
        this.subscribers = new ArrayList<>();
    }

    public static <State> void setInstance(Store<State> store) {
        if (instance == null) {
            instance = store;
            System.out.println("Store instance was set.");
        } else {
            System.out.println("Store instance has already been set.");
        }
    }

    // if we want multiple stores at some point, the interface can remain the same
    public static <State> Store<State> get() {
        if (instance == null) {
            System.out.println("Tried to access null store.");
        }
        return (Store<State>) instance;
    }

    public State getState() {
        return this.state;
    }

    public synchronized void dispatch(Action action) {
        State newState = reducer.reduce(this.state, action);

        if(this.state != newState && !this.state.equals(newState)) {
            this.state = newState;
            subscribers.forEach(subscriber -> { subscriber.onChange(this.state); });
        }
    }

    public void subscribe(Subscriber<State> subscriber) {
        subscribers.add(subscriber);
        subscriber.onChange(this.state);
    }
}
