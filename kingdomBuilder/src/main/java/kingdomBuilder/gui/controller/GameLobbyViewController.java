package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameLobbyView.
 */
public class GameLobbyViewController extends Controller implements Initializable {

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Sets the functionality for the GameStarted Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    public void onButtonGameStartPressed(Event event) {
        sceneLoader.showGameView();
    }

    /**
     * Sets the functionality for the GameList Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    public void onButtonGameListPressed(Event event) {
        sceneLoader.showGameSelectionView();
    }
}
