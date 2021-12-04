package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class GameViewController extends Controller {

    private MainViewController mainViewController;

    @FXML
    private Label player1;

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
        //resize the subscene to the size of its parent (game_hbox)
        game_subscene.heightProperty().bind(game_hbox.heightProperty());
        game_subscene.widthProperty().bind(game_hbox.widthProperty());
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public SubScene getGame_subscene() {return this.game_subscene;}
}
