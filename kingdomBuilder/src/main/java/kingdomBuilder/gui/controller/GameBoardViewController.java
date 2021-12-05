package kingdomBuilder.gui.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import kingdomBuilder.gui.gameboard.HexagonTile;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardViewController implements Initializable {

    private HexagonTile[][] gameBoard = new HexagonTile[20][20];

    @FXML
    private Pane gameboard_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //gameboard_pane.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
        setupGameBoard();
        setupEventHandler();
    }

    private void setupGameBoard() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                double xPos;
                if (y % 2 == 0) {
                    xPos = x * 70;
                } else {
                    xPos = x * 70 + 35;
                }

                double yPos = y * 60;
                HexagonTile hexagonTile = new HexagonTile(xPos, yPos);
                gameboard_pane.getChildren().add(hexagonTile);
                gameBoard[x][y] = hexagonTile;
            }
        }
    }

    private void setupEventHandler() {
        setupScrollEventHandler();
    }

    private void setupScrollEventHandler() {
        gameboard_pane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();

                if (deltaY < 0){
                    zoomFactor = 0.95;
                }
                gameboard_pane.setScaleX(gameboard_pane.getScaleX() * zoomFactor);
                gameboard_pane.setScaleY(gameboard_pane.getScaleY() * zoomFactor);
                event.consume();
            }
        });
    }
}