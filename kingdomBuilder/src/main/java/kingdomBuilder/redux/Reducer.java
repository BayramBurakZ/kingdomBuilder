package kingdomBuilder.redux;

public interface Reducer<State> {
    State reduce(Store<State> store, State oldState, Action action);
}