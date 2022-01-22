package kingdomBuilder.redux;

import java.util.Set;

/**
 * Interface for the generated deferred state.
 * @param <T> the type of the state.
 */
public interface DeferredState<T> {
    Set<String> getChangedAttributes();
    T withChanges();
}
