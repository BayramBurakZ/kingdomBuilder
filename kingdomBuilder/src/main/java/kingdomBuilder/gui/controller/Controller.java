package kingdomBuilder.gui.controller;

import java.lang.reflect.Field;

/**
 * Basic class that every controller inherit so every ViewController is able to cast into this.
 */
public class Controller {
    Field[] fields;
    public Controller() {
        fields = getClass().getFields();
    }
}
