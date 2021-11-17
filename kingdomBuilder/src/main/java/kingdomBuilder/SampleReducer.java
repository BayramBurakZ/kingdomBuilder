package kingdomBuilder;

import kingdomBuilder.redux.Action;
import kingdomBuilder.redux.Reducer;

public class SampleReducer implements Reducer<SampleState> {

    @Override
    public SampleState reduce(SampleState state, Action action) {
        if(action instanceof IncrementAction) {
            var counter = state.getCounter();
            return state.withCounter(counter + 1);
        }

        return state;
    }

}
