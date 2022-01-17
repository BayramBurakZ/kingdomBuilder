package kingdomBuilder.redux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents the store of the application.
 * @param <State> State of application.
 */
public class Store<State> {
    /**
     * Represents the state of the application.
     */
    private State state;
    /**
     * Represents the reducer of the application.
     */
    private final Reducer<State> reducer;
    /**
     * Represents the list of subscribers which should be notified if the state is modified.
     */
    private final List<Subscriber<State>> subscribers;

    private HashMap<String, Set<Consumer<State>>> subscribers1;

    /**
     * Creates a new Store object with the given state and reducer.
     * @param state State of the application.
     * @param reducer Reducer of the application.
     */
    public Store(State state, Reducer<State> reducer) {
        this.state = state;
        this.reducer = reducer;
        this.subscribers = new ArrayList<>();
    }

    /**
     * Gets the state of the application.
     * @return the state of application.
     */
    public State getState() {
        return this.state;
    }

    /**
     * Calls the reducer to get the new state and notifies all subscribers.
     * @param action changes the state.
     */
    public synchronized void dispatch(Action action) {
        State newState = reducer.reduce(this, this.state, action);

        if(this.state != newState && !this.state.equals(newState)) {
            this.state = newState;
            subscribers.forEach(subscriber -> { subscriber.onChange(this.state); });
        }
    }

    /**
     * Adds a new subscriber to the {@link #subscribers}.
     * @param subscriber subscriber of the state that should be notified if the state changes.
     */
    public void subscribe(Subscriber<State> subscriber) {
        subscribers.add(subscriber);
        subscriber.onChange(this.state);
    }

    /**
     * Adds a new subscriber to the {@link #subscribers1}.
     * @param subscriber subscriber of the state that should be notified if the state changes.
     */
    public <T> void subscribe(Consumer<State> subscriber, Supplier<T>... suppliers) {
        for(var supplier: suppliers) {
            String name = supplier.getClass().getName();
            var consumerSet = subscribers1.get(name);
            consumerSet.add((Consumer<State>)supplier);
        }
    }

    /**
     *
     * @param changedAttributes represents a list of attributes that were changed.
     */
    public void notifyChanged(List<String> changedAttributes) {
        changedAttributes.forEach(m->{
            var consumerSet = subscribers1.get(m);
            consumerSet.forEach(n->{
                //TODO
            });
        });
    }
}
