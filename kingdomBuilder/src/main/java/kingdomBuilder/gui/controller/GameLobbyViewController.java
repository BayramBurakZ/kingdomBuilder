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

    @FXML
    private TextField gameLobbyTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandler();
    }

    @FXML
    public void onButtonGameStartPressed(Event event) {
        String playerName = gameLobbyTextField.getText();
        if (!playerName.isEmpty()) {
            mainViewController.showGameView(playerName);
        }
    }

    private void setupEventHandler() {
        setupKeyEventHandler();
    }

    private void setupKeyEventHandler() {
        gameLobbyTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String playerName = gameLobbyTextField.getText();
                if (!playerName.isEmpty() && event.getCode() == KeyCode.ENTER) {
                    mainViewController.showGameView(playerName);
                }
            }
        });
    }

    @FXML
    public void onButtonMainMenuPressed(Event event) {
        mainViewController.showMenuView();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
