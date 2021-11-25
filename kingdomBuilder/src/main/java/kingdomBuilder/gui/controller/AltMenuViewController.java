package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;

public class AltMenuViewController {

    private AltMainViewController mainViewController;

    @FXML
    public void onButtonNewGamePressed(Event event) {
        mainViewController.showGameLobbyView();
    }

    public void setMainViewController(AltMainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
