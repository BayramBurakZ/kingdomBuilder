package kingdomBuilder.gui.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import kingdomBuilder.gui.gameboard.HexagonTile;
import javafx.scene.input.KeyEvent;
import kingdomBuilder.model.Model;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the board that shows the hexagon tiles and the current board.
 */
public class GameBoardViewController implements Initializable {

    //temporary solution to store the board
    private Model model = new Model();
    private HexagonTile[][] gameBoard = model.getGameboard_model();

    /**
     * Represents the pane for all hexagons
     */
    @FXML
    private Pane gameboard_pane;

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //gameboard_pane.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
        setupGameBoard();
        setupEventHandler();
    }

    /**
     * Generates the 20 x 20 field of the hexagons
     */
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
                //get texture from the pre-generated gameboard, later get Tile Type and then set texture
                hexagonTile.setFill(gameBoard[x][y].getFill());

                gameboard_pane.getChildren().add(hexagonTile);
                //gameBoard[x][y] = hexagonTile;
            }
        }
    }

    /**
     * Setup all connected EventHandler
     */
    private void setupEventHandler() {
        setupScrollEventHandler();
        setupKeyEventHandler();
    }

    /**
     * Creates the EventHandler that is responsible for the scrolling Event
     */
    private void setupScrollEventHandler() {
        gameboard_pane.setOnScroll(new EventHandler<ScrollEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is registered happens.
             * @param event the event which occurred
             */
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

    /**
     * Translates the gameboard when the user presses the arrow keys
     * @author Tom & Linda
     */
    private void setupKeyEventHandler() {
        gameboard_pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is registered happens.
             * @param event the event which occurred
             */
            @Override
            public void handle(KeyEvent event) {
                switch(event.getCode()){
                    // TODO: Verschiebungsgrad anpassen
                    case UP:
                        gameboard_pane.setTranslateY(gameboard_pane.getTranslateY() + 20.0);
                        break;
                    case DOWN:
                        gameboard_pane.setTranslateY(gameboard_pane.getTranslateY() - 20.0);
                        break;
                    case LEFT:
                        gameboard_pane.setTranslateX(gameboard_pane.getTranslateX() + 20.0);
                        break;
                    case RIGHT:
                        gameboard_pane.setTranslateX(gameboard_pane.getTranslateX() - 20.0);
                        break;
                }
            }
        });
    }
}
