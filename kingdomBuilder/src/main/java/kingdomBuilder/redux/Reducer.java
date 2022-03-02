package kingdomBuilder.redux;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public class Reducer<State> {
    private final HashMap<String, BiFunction<Store<State>, Object, DeferredState<State>>> reducers;

    public Reducer() {
        reducers = new HashMap<>();
    }

    public Reducer(HashMap<String, BiFunction<Store<State>, Object, DeferredState<State>>> reducers) {
        this.reducers = reducers;
    }

    public HashMap<String, BiFunction<Store<State>, Object, DeferredState<State>>> getReducers() {
        return reducers;
    }

    DeferredState<State> reduce(Store<State> store, String action, Object payload) {
        final var reducer = reducers.getOrDefault(action, null);
        if(reducer == null) return null;
        return reducer.apply(store, payload);
    }

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

    private DeferredState<State> invoke(Method m, Store<State> store, Object payload) {
        try {
            return (DeferredState<State>) m.invoke(this, store, store.getState(), payload);
        } catch(Exception unused) {
            unused.printStackTrace();
            return null;
        }
    }

}