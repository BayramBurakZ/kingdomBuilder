package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.*;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameSelectionView.
 */
public class GameSelectionViewController extends Controller implements Initializable {

    /**
     * Layout that contains the games table (left side) and the preview of a single game (right side)
     */
    @FXML
    private HBox hbox_gameselection;

    /**
     * Layout that contains the games table with its buttons
     */
    @FXML
    private VBox vbox_table;

    /**
     * Layout that contains the preview of a game
     */
    @FXML
    private VBox vbox_preview;

    /**
     * Constructs the GameSelectionViewController.
     * @param store The Application's store to set the field.
     */
    public GameSelectionViewController(Store<KBState> store) {
        super.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupLayout();
    }

    /**
     * Sets the functionality for the CreateNewGame Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onButtonCreateNewGamePressed(Event event) {
        sceneLoader.showGameSettingsView(true);
    }

    @FXML
    private void onLevelEditorButtonPressed() {
        sceneLoader.showLevelEditorView();
    }

    /**
     * Sets the functionality for the ReturnToMenu Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onReturnToMenuButtonPressed(Event event) {
        store.dispatch(new DisconnectAction());
        sceneLoader.showMenuView();
    }

    /**
     * Sets the functionality for the CreateNewGame Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    private void onJoinGamePressed(Event event) {
        //TODO: Network send "join" Message
        sceneLoader.showGameView(false, true);
    }

    /**
     * Initializes layout arrangement.
     */
    private void setupLayout() {
        vbox_table.prefWidthProperty().bind(hbox_gameselection.widthProperty().multiply(0.5));
        vbox_preview.prefWidthProperty().bind(hbox_gameselection.widthProperty().multiply(0.5));
    }
}