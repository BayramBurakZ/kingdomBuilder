package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameViewController {

    private MainViewController mainViewController;

    @FXML
    private Label player1;

    @FXML
    private SubScene subSceneBoard;

    @FXML
    private BorderPane borderPane;

    @FXML
    public void onButtonMainMenuPressed(Event event) {
        mainViewController.showMenuView();
    }

    @FXML
    public void initialize() {
        //set height and width of the subscene to the borderpanes center
        subSceneBoard.heightProperty().set(borderPane.getCenter().getBoundsInParent().getHeight());
        subSceneBoard.widthProperty().set(borderPane.getCenter().getBoundsInParent().getWidth());
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    // TODO: Möglicherweise wegen REDUX überflüssig, dass Daten zwischen den Controllern hin und her geschoben werden
    public void setPlayer1Name(String name) {
        player1.setText(name);
    }
}
