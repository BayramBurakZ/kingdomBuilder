package kingdomBuilder.redux;

/**
 * Represents a subscriber, which gets notified on state changes.
 * @param <State> the type of state this subscriber subscribes to.
 */
@FunctionalInterface
public interface Subscriber<State> {

    /**
     * Used to notify the subscriber about changes.
     * @param state a state containing the latest changes.
     */
    void onChange(State state);
}
