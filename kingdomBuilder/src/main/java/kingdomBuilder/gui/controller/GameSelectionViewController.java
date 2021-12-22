package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameSelectionView.
 */
public class GameSelectionViewController extends Controller implements Initializable {
    /**
     * Represents the MainViewController for access to switch Views-methods.
     */
    private MainViewController mainViewController;

    @FXML
    private HBox hbox_gameselection;

    @FXML
    private VBox vbox_table;

    @FXML
    private VBox vbox_preview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vbox_table.prefWidthProperty().bind(hbox_gameselection.widthProperty().multiply(0.5));
        vbox_preview.prefWidthProperty().bind(hbox_gameselection.widthProperty().multiply(0.5));
    }

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