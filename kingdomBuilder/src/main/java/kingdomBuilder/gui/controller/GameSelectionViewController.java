package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;

public class GameSelectionViewController extends Controller {

    private MainViewController mainViewController;

    @FXML
    public void onButtonCreateNewGamePressed(Event event) {
        mainViewController.showGameLobbyView();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}