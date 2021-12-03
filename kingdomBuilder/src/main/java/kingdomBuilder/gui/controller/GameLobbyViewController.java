package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class GameLobbyViewController extends Controller {

    private MainViewController mainViewController;

    @FXML
    private TextField gameLobbyTextField;

    @FXML
    public void onButtonGameStartPressed(Event event) {
        String playerName = gameLobbyTextField.getText();
        if (playerName.isEmpty()) {

        } else {
            mainViewController.showGameView(playerName);
        }
    }

    @FXML
    public void onButtonMainMenuPressed(Event event) {
        mainViewController.showMenuView();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
