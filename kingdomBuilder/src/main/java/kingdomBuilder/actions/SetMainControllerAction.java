package kingdomBuilder.actions;

import kingdomBuilder.gui.controller.MainViewController;
import kingdomBuilder.redux.Action;

public class SetMainControllerAction extends Action {
    public MainViewController controller;

    public SetMainControllerAction(MainViewController controller) { this.controller = controller; }
}
