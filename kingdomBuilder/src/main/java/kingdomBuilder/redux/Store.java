package kingdomBuilder.redux;

import java.util.ArrayList;
import java.util.List;

public class Store<State> {
    private State state;
    private final Reducer<State> reducer;
    private List<Subscriber<State>> subscribers;

    public Store(State state, Reducer<State> reducer) {
        this.state = state;
        this.reducer = reducer;
        this.subscribers = new ArrayList<>();
    }

    public State getState() {
        return this.state;
    }

    public void dispatch(Action action) {
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
