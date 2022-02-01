package kingdomBuilder.redux;

public interface Reducer<State> {
    DeferredState reduce(Store<State> store, State state, Action action);
}