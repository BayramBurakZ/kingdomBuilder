package kingdomBuilder.network;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Utility class, which allows one to subscribe an arbitrary amount of subscriber,
 * which conform to the Consumer interface.
 * @param <T> The event payload dispatched.
 */
public class Event<T> {
    private final Set<Consumer<T>> subscriber = new HashSet<>();

    public Consumer<T> subscribe(Consumer<T> consumeSubscriber) {
        subscriber.add(consumeSubscriber);
        return consumeSubscriber;
    }

    public void unsubscribe(Consumer<T> consumeSubscriber) {
        subscriber.remove(consumeSubscriber);
    }

    public void clear() {
        subscriber.clear();
    }

    public void dispatch(T payload) {
        subscriber.forEach(c -> c.accept(payload));
    }

}
