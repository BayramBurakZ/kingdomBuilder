package kingdomBuilder.gui.controller;

import kingdomBuilder.KBState;
import kingdomBuilder.redux.Store;
import kingdomBuilder.redux.Subscriber;

import java.lang.reflect.Field;

/**
 * Basic class that every controller inherit so every ViewController is able to cast into this.
 */
public class Controller {
    /**
     * Represents the store of the application.
     */
    private Store<KBState> store;
    Field[] fields;
    public Controller() {
        fields = getClass().getFields();
        for(var field: fields){
            store.subscribe((Subscriber<KBState>) this, field.getName());
        }
    }
}
