package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLobbyViewController extends Controller implements Initializable {

    private MainViewController mainViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    public void onButtonGameStartPressed(Event event) {
        mainViewController.showGameView();
    }

    @FXML
    public void onButtonMainMenuPressed(Event event) {
        mainViewController.showMenuView();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
