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

/**
 * This class controls the functions of the IAmView.
 */
public class IAmViewController extends Controller implements Initializable {
    /**
     * Represents the MainViewController for access to switch Views-methods.
     */
    private MainViewController mainViewController;
    /**
     * Represents the store of the application.
     */
    private final Store<KBState> store;

    /**
     * Represents the TextField to enter the preferred name.
     */
    @FXML
    private TextField iAmViewTextField;

    /**
     * Constructor that sets the Store.
     * @param store
     */
    public IAmViewController(Store<KBState> store) {
        this.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandler();
    }

    /**
     * Sets the MainViewController.
     * @param mainViewController MainViewController with all functions.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    /**
     * Sets the functionality for the MainMenu Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    public void onButtonMainMenuShow(Event event) {
        SetPreferredName();
    }

    /**
     * Setup all connected EventHandler.
     */
    private void setupEventHandler() {
        setupKeyEventHandler();
    }

    /**
     * Creates the EventHandler that is responsible for the Key events.
     */
    private void setupKeyEventHandler() {
        iAmViewTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is registered happens.
             * @param event the event which occurred.
             */
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    SetPreferredName();
                }
            }
        });
    }

    /**
     * Sets the user's preferred name.
     */
    private void SetPreferredName() {
        String preferredName = iAmViewTextField.getText().trim();
        if (!preferredName.isEmpty()) {
            // TODO: could let dispatch throw in case the name isn't valid
            store.dispatch(new SetPreferredNameAction(preferredName));
            mainViewController.showMenuView();
        }
    }
}
