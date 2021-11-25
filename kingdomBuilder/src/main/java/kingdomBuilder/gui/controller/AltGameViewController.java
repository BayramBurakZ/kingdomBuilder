package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AltGameViewController {

    private AltMainViewController mainViewController;

    @FXML
    private Label player1;

    @FXML
    public void onButtonMainMenuPressed(Event event) {
        mainViewController.showMenuView();
    }

    public void setMainViewController(AltMainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    // Möglicherweise wegen REDUX überflüssig, dass Daten zwischen den Controllern hin und her geschoben werden
    public void setPlayer1Name(String name) {
        player1.setText(name);
    }
}
