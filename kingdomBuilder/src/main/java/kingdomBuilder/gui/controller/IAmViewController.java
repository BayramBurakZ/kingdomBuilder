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

public class IAmViewController extends Controller implements Initializable {
    private MainViewController mainViewController;

    @FXML
    private TextField iAmViewTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandler();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    public void onButtonMainMenueShow(Event event) {
        //TODO: send Name from iAmViewTextField to Modul to safe it, maybe create a new client without ID
        if(!iAmViewTextField.getText().isEmpty())
            mainViewController.showMenuView();
    }

    private void setupEventHandler() {
        setupKeyEventHandler();
    }

    private void setupKeyEventHandler() {
        iAmViewTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String playerName = iAmViewTextField.getText();
                if (!playerName.isEmpty() && event.getCode() == KeyCode.ENTER) {
                    mainViewController.showMenuView();
                }
            }
        });
    }
}
