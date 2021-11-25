package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AltGameLobbyViewController {

    private AltMainViewController altMainViewController;

    @FXML
    private TextField gameLobbyTextField;

    @FXML
    public void onButtonGameStartPressed(Event event) {
        String playerName = gameLobbyTextField.getText();
        if (playerName.isEmpty()) {

        } else {
            altMainViewController.showGameView(playerName);
        }
    }

    @FXML
    public void onButtonMainMenuPressed(Event event) {
        altMainViewController.showMenuView();
    }

    public void setMainViewController(AltMainViewController altMainViewController) {
        this.altMainViewController = altMainViewController;
    }
}
