package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameView.
 */
public class GameViewController extends Controller implements Initializable {
    /**
     * Represents the MainViewController for access to switch Views-methods.
     */
    private MainViewController mainViewController;

    /**
     * Represents the label for player one.
     */
    @FXML
    private Label game_label_player1;

    /**
     * Represents the HBox that contains the game.
     */
    @FXML
    private HBox game_hbox;

    /**
     * Represents the SubScene to show the board.
     */
    @FXML
    private SubScene game_subscene;

    /**
     * Sets the functionality for the MainMenu Button.
     * @param event Contains the data from the event source.
     */
    @FXML
    public void onButtonMainMenuPressed(Event event) {
        mainViewController.showMenuView();
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //resize the subscene to the size of its parent (game_hbox)
        game_subscene.heightProperty().bind(game_hbox.heightProperty());
        game_subscene.widthProperty().bind(game_hbox.widthProperty());
    }

    /**
     * Sets the MainViewController.
     * @param mainViewController MainViewController with all functions.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    /**
     * Gets the SubScene for the game board.
     * @return SubScene for displaying the game board
     */
    public SubScene getGame_subscene() {return this.game_subscene;}
}
