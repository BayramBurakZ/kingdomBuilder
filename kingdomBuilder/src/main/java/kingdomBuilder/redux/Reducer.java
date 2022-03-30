package kingdomBuilder.redux;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Represents a reducer class, which combines multiple reducers into a semantic group.
 * Used internal to aggregate all reducers into one object.
 * @param <State> The type of the state this reducer operates on.
 */
public class Reducer<State> {
    private final HashMap<String, BiFunction<Store<State>, Object, DeferredState<State>>> reducers;

    /**
     * Initializes the internal reducers with an empty map.
     */
    public Reducer() {
        reducers = new HashMap<>();
    }

    /**
     * Initializes the internal reducers map with given reducers.
     * @param reducers A map of reducers now handled by this instance.
     */
    public Reducer(HashMap<String, BiFunction<Store<State>, Object, DeferredState<State>>> reducers) {
        this.reducers = reducers;
    }

    /**
     * {@return all found reducers.}
     */
    public HashMap<String, BiFunction<Store<State>, Object, DeferredState<State>>> getReducers() {
        return reducers;
    }

    /**
     * Determines the reducer-method associated with the given action and invokes said method possibly mutating
     * the state in the process.
     * @param store the store containing the state on which the reducer operates.
     * @param action the action to determine the method for.
     * @param payload nullable payload (i.e. parameter) passed to the reducer.
     * @return An instance of {@link DeferredState} containing a set of "transactions" issued by the reducers.
     */
    DeferredState<State> reduce(Store<State> store, String action, Object payload) {
        final var reducer = reducers.get(action);
        return reducer.apply(store, payload);
    }

    /**
     * Leverages reflection to collect all methods marked as reducers from an inheriting class.
     * @param obj the instance of the inheriting class.
     * @param <T> the actual type of the inheriting class.
     */
    protected <T> void registerReducers(T obj) {
        List<Method> methods = List.of(obj.getClass().getMethods());

        for(var method: methods) {
            if(!method.isAnnotationPresent(Reduce.class))
                continue;

            final Reduce annotation = method.getAnnotation(Reduce.class);
            final String action = annotation.action();

            reducers.put(action, (state, payload) -> invoke(method, state, payload));
        }
    }

    /**
     * Utility method used to invoke reducer-methods obtained via reflection.
     * @param m the reducer method obtained via reflection.
     * @param store the store containing the state on which the reducer operates.
     * @param payload the payload associated with the action.
     * @return an instance of {@code DeferredState} containing a set of "transactions" issued by the reducers.
     */
    private DeferredState<State> invoke(Method m, Store<State> store, Object payload) {
        try {
            return (DeferredState<State>) m.invoke(this, store, store.getState(), payload);
        } catch(Exception unused) {
            unused.printStackTrace();
            return null;
        }
    }

}