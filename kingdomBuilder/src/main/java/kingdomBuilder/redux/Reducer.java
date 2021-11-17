package kingdomBuilder.redux;

public interface Reducer<State> {
    State reduce(State oldState, Action action);
}