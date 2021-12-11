package kingdomBuilder;

import javafx.application.Application;

import java.io.IOException;

import kingdomBuilder.gui.KingdomBuilderApplication;
import kingdomBuilder.reducers.KBReducer;
import kingdomBuilder.redux.Store;

public class Boot {

    public static void main(String[] args) throws IOException {

        Store<KBState> store = new Store<>(new KBState(), new KBReducer());
        Store.setInstance(store);

        Application.launch(KingdomBuilderApplication.class);
    }
}
