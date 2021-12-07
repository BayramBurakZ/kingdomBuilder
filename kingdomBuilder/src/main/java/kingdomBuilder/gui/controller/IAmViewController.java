package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kingdomBuilder.actions.SetClientIDAction;
import kingdomBuilder.actions.SetClientNameAction;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

public class IAmViewController extends Controller implements Initializable {
    private MainViewController mainViewController;
    private Store store;

    @FXML
    private TextField iAmViewTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store = Store.get();
        setupEventHandler();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    public void onButtonMainMenueShow(Event event) {
        if(iAmViewTextField.getText().isEmpty())
            return;

        String name = iAmViewTextField.getText().trim();

        store.dispatch(new SetClientNameAction(name));
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
                    store.dispatch(new SetClientNameAction(playerName));
                    mainViewController.showMenuView();
                }
            }
        });
    }
}
