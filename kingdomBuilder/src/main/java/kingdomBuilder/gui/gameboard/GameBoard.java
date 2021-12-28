package kingdomBuilder.gui.gameboard;

import javafx.scene.layout.Pane;
import kingdomBuilder.KBState;
import kingdomBuilder.model.TileType;
import kingdomBuilder.redux.Store;

/**
 * This class is used to contains all functions in terms of the boards gui.
 */
public class GameBoard {
    /*
    /**
     * Represents the store of the application.
     *\/
    * private final Store<KBState> store;
    */

    /**
     * Represents the board that contains the hexagons.
     */
    private HexagonTile[][] gameBoard = new HexagonTile[20][20];

    /**
     * Constructor to instantiate the GameBoard.
     */
    public GameBoard() { }

    /**
     * Setup the GameBoard with textured hexagons.
     * @param pane The pane on which the hexagons are drawn.
     * @param gameBoardData The data with all information.
     */
    public void setupGameBoard(Pane pane, TileType[][] gameBoardData) {
        //TODO: reads the array of Tiles in the store and creates the board accordingly.
        // instead of using the data from the parameter
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                int xPos;
                if (y % 2 == 0) {
                    xPos = x * 70;
                } else {
                    xPos = x * 70 + 35;
                }

                int yPos = y * 60;

                HexagonTile hexagon = new HexagonTile(xPos, yPos, gameBoardData[x][y]);
                pane.getChildren().add(hexagon);
                gameBoard[x][y] = hexagon;
            }
        }
    }

    public void highlightTerrain( ) {
        // TODO: checks which hexagons are able to build on.
    }
}
