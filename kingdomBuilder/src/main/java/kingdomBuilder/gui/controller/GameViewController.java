package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GameViewController {

    private MainViewController mainViewController;

    @FXML
    private Label player1;

    @FXML
    private BorderPane game_boarderpane;

    @FXML
    private HBox game_hbox;

    @FXML
    private SubScene game_subscene;

    @FXML
    public void onButtonMainMenuPressed(Event event) {
        mainViewController.showMenuView();
    }

    @FXML
    public void initialize() {
        //set height and width of the subscene to the borderpanes center
        /*
        game_subscene.heightProperty().set(game_boarderpane.getCenter().getBoundsInParent().getHeight());
        game_subscene.widthProperty().set(game_boarderpane.getCenter().getBoundsInParent().getWidth());

         */

        // TODO: subscene/hbox/boarderpane_center resize, gets biggern, not smaller

        game_subscene.heightProperty().bind(game_hbox.heightProperty());
        game_subscene.widthProperty().bind(game_hbox.widthProperty());
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    // TODO: Möglicherweise wegen REDUX überflüssig, dass Daten zwischen den Controllern hin und her geschoben werden
    public void setPlayer1Name(String name) {
        player1.setText(name);
    }
}
