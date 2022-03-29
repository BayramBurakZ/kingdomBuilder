package kingdomBuilder.redux;

import java.util.Set;

/**
 * Interface for the generated deferred state.
 * @param <T> the type of the state.
 */
public interface DeferredState<T> {
    /**
     * Gets all the attributes that are changed.
     *
     * @return all changed attributes.
     */
    Set<String> getChangedAttributes();

    /**
     * Returns the state with changes.
     *
     * @return the state that is changed.
     */
    T withChanges();
}
