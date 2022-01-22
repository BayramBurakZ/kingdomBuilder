package kingdomBuilder.redux;

import javafx.application.Platform;

import java.util.*;

/**
 * Represents the store of the application.
 * @param <State> state of application.
 */
public class Store<State> {
    private static final String ATTRIBUTE_WILDCARD = "*";

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
    private final Map<String, Set<Subscriber<State>>> subscribers;

    /**
     * Creates a new Store object with the given state and reducer.
     * @param state state of the application.
     * @param reducer reducer of the application.
     */
    public Store(State state, Reducer<State> reducer) {
        this.state = state;
        this.reducer = reducer;
        this.subscribers = new HashMap<>();
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
        Platform.runLater(() -> {
            final DeferredState<State> state = reducer.reduce(this, this.state, action);
            if(state.getChangedAttributes().isEmpty()) return;

            Set<Subscriber<State>> subscribers = new HashSet<>();
            subscribers.addAll(getSubscriberSet(ATTRIBUTE_WILDCARD));
            for(String attribute: state.getChangedAttributes())
                subscribers.addAll(getSubscriberSet(attribute));

            this.state = state.withChanges();
            subscribers.forEach(s -> s.onChange(this.state));
        });
    }

    /**
     * Subscribes an entity, which is then informed, when the state changes.
     * @param subscriber
     * @param attributes
     */
    public void subscribe(Subscriber<State> subscriber, String... attributes) {
        final Set<String> attributeSet = Set.of(attributes);

        if(attributeSet.isEmpty()) {
            var subs = getSubscriberSet(ATTRIBUTE_WILDCARD);
            subs.add(subscriber);
            return;
        }

        for(var attribute: attributeSet) {
            var subs = getSubscriberSet(attribute);
            subs.add(subscriber);
        }

        subscriber.onChange(this.state);
    }

    /**
     * Helper method, which retrieves the set of subscribers or creates it if absent.
     * @param name the name of the attribute, the subscribers listen too.
     * @return The set of subscribers that listen to the specified attribute.
     */
    private Set<Subscriber<State>> getSubscriberSet(String name) {
        return subscribers.computeIfAbsent(
            name,
            (String unused) -> new HashSet<>()
        );
    }
}
