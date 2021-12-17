package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;

/**
 * This class controls all functions for the GameSelectionView.
 */
public class GameSelectionViewController extends Controller {
    /**
     * Represents the MainViewController for access to switch Views-methods.
     */
    private MainViewController mainViewController;

    /**
     * Sets the functionality for the CreateNewGame Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    public void onButtonCreateNewGamePressed(Event event) {
        mainViewController.showGameLobbyView();
    }

    /**
     * Sets the MainViewController.
     * @param mainViewController MainViewController with all functions.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}