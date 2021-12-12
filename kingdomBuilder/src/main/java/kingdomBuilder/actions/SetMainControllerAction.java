package kingdomBuilder.actions;

import kingdomBuilder.gui.controller.MainViewController;
import kingdomBuilder.redux.Action;

/**
 * Action to set the MainViewController in the {@link kingdomBuilder.KBState state} of the store. So every View is
 * modifiable and all their methods are callable.
 */
public class SetMainControllerAction extends Action {
    /**
     * field for the MainViewController so the reducer has access to it
     */
    public MainViewController controller;

    /**
     * Constructor that creates a new SetMainControllerAction so the reducer can modify the controller inside the state
     * @param controller the MainViewController that should be set inside the state
     */
    public SetMainControllerAction(MainViewController controller) { this.controller = controller; }
}
