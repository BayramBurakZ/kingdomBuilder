package kingdomBuilder.gui.gameboard;

import javafx.scene.layout.Pane;
import kingdomBuilder.KBState;
import kingdomBuilder.model.TileType;
import kingdomBuilder.redux.Store;

import java.util.ResourceBundle;

/**
 * This class is used to contain all functions in terms of the boards gui.
 */
public class GameBoard {
    /**
     * Represents the store of the application.
     */
    private final Store<KBState> store;

    /**
     * Represents the board that contains the hexagons.
     */
    private HexagonTile[][] gameBoard = new HexagonTile[20][20];

    /**
     * Constructor to instantiate the GameBoard.
     * @param store The Store to access the state.
     */
    public GameBoard(Store<KBState> store) {
        this.store = store;
    }

    /**
     * Represents the resourceBundle that used for language support.
     */
    private static ResourceBundle resourceBundle;

    /**
     * Setup the GameBoard with textured hexagons.
     * @param pane The pane on which the hexagons are drawn.
     * @param gameBoardData The data with all information.
     * @param resource The ResourceBundle for translating text.
     */
    public void setupGameBoard(Pane pane, TileType[][] gameBoardData, ResourceBundle resource) {
        resourceBundle = resource;

        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                int xPos;
                if (y % 2 == 0) {
                    xPos = x * 70;
                } else {
                    xPos = x * 70 + 35;
                }

                int yPos = y * 60;

                HexagonTile hexagon = new HexagonTile(xPos, yPos, gameBoardData[x][y], resource);
                pane.getChildren().add(hexagon);
                gameBoard[x][y] = hexagon;
            }
        }
    }

    /**
     * Highlights Hexagons on the map which matches the given type.
     */
    public void highlightTerrain() {
        // TODO: remove randomizer
        int random = (int) (Math.random() * 5) + 1;
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard.length; x++) {
                // remove hightlight
                if (gameBoard[x][y].isHighlighted()) {
                    gameBoard[x][y].removeHighlight();
                }

                // set new highlight
                //TODO: Change to Gamelogic
                if (gameBoard[x][y].getTileType() == TileType.valueOf(random)) {
                    gameBoard[x][y].setHighlight();
                }
            }
        }
    }
}
