package kingdomBuilder.network.util;

import java.util.Set;
import java.util.function.Consumer;

public class Event<T> {
    private Set<Consumer<T>> consumers;

    public Consumer<T> subscribe(Consumer<T> c) {
        consumers.add(c);
        return c;
    }

    public void unsubscribe(Consumer<T> c) {
        consumers.remove(c);
    }

    public void dispatch(T payload) {
        consumers.forEach(c -> c.accept(payload));
    }
}
