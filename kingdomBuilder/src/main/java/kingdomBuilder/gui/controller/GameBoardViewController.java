package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardViewController implements Initializable {

    @FXML
    private Pane gameboard_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameboard_pane.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
    }
}
