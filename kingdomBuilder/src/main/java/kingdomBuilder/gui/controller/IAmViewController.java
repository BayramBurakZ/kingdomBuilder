package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.SetPreferredNameAction;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

public class IAmViewController extends Controller implements Initializable {
    private MainViewController mainViewController;
    private final Store<KBState> store;

    @FXML
    private TextField iAmViewTextField;

    public IAmViewController(Store<KBState> store) {
        this.store = store;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        store.dispatch(new SetPreferredNameAction(name));
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
                    store.dispatch(new SetPreferredNameAction(playerName));
                    mainViewController.showMenuView();
                }
            }
        });
    }
}
