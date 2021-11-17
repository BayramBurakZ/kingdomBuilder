package kingdomBuilder;

public class SampleState {
    private final Integer counter;

    public SampleState(int value) {
        this.counter = value;
    }

    public Integer getCounter() {
        return counter;
    }

    public SampleState withCounter(int value) {
        if(counter == value) return this;
        return new SampleState(value);
    }
}
