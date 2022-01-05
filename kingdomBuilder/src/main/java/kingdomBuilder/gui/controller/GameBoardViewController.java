package kingdomBuilder.gui.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import kingdomBuilder.gui.gameboard.GameBoard;
import javafx.scene.input.KeyEvent;
import kingdomBuilder.model.Model;
import kingdomBuilder.model.TileType;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the board that shows the hexagon tiles and the current board.
 */
public class GameBoardViewController extends Controller implements Initializable {

    //TODO:
    // temporary solution to store the board instead using the data from dataLogic
    private TileType[][] gameBoardData;

    /**
     * Represents the gameBoard with data for the gui like hexagons and textures.
     */
    private GameBoard gameBoard = new GameBoard(store);

    /**
     * Represents the pane for all hexagons
     */
    @FXML
    private Pane gameBoard_pane;

    /**
     * Represents the resourceBundle that used for language support.
     */
    private ResourceBundle resourceBundle;

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //make board transparent
        //gameBoard_pane.setOpacity(0.0);

        resourceBundle = resources;

        // TODO: Remove
        Model model = new Model();
        gameBoardData = model.getGameBoardData();

        setupGameBoard();
        setupEventHandler();
    }

    /**
     * Generates the 20 x 20 field of the hexagons.
     */
    private void setupGameBoard() {
        // TODO: access data via state
        gameBoard.setupGameBoard(gameBoard_pane, gameBoardData, resourceBundle);
    }

    /**
     * Setup all connected EventHandler.
     */
    private void setupEventHandler() {
        setupScrollEventHandler();
        setupKeyEventHandler();
    }

    /**
     * Creates the EventHandler that is responsible for the scrolling Event.
     */
    private void setupScrollEventHandler() {
        gameBoard_pane.setOnScroll(new EventHandler<ScrollEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is registered happens.
             * @param event the event which occurred.
             */
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();

                if (deltaY < 0){
                    zoomFactor = 0.95;
                }
                gameBoard_pane.setScaleX(gameBoard_pane.getScaleX() * zoomFactor);
                gameBoard_pane.setScaleY(gameBoard_pane.getScaleY() * zoomFactor);
                event.consume();
            }
        });
    }

    /**
     * Translates the gameboard when the user presses the arrow keys.
     * @author Tom & Linda
     */
    private void setupKeyEventHandler() {
        gameBoard_pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Invoked when a specific event of the type for which this handler is registered happens.
             * @param event the event which occurred.
             */
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    // TODO: Verschiebungsgrad anpassen
                    case UP -> gameBoard_pane.setTranslateY(gameBoard_pane.getTranslateY() + 20.0);
                    case DOWN -> gameBoard_pane.setTranslateY(gameBoard_pane.getTranslateY() - 20.0);
                    case LEFT -> gameBoard_pane.setTranslateX(gameBoard_pane.getTranslateX() + 20.0);
                    case RIGHT -> gameBoard_pane.setTranslateX(gameBoard_pane.getTranslateX() - 20.0);
                    //ToDO: remove - just for testing the highlight
                    case R ->  gameBoard.highlightTerrain();
                }
            }
        });
    }
}
