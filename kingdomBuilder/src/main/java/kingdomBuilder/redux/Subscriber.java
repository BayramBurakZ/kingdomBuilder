package kingdomBuilder.redux;

public interface Subscriber<State> {
    void onChange(State state);
}
