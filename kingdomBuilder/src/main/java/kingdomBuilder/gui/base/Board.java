package kingdomBuilder.gui.base;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.gui.gameboard.HexagonTile;
import kingdomBuilder.model.TileType;
import kingdomBuilder.redux.Store;

import java.util.ResourceBundle;

/**
 * Abstract class to generate a board.
 */
public abstract class Board {
    /**
     * Represents the store of the application.
     */
    private final Store<KBState> store;

    /**
     * Represents the resourceBundle that used for language support.
     */
    private static ResourceBundle resourceBundle;

    /**
     * Represents the board that contains the hexagons.
     */
    // TODO: use constants from Datalogic
    protected HexagonTile[][] gameBoard = new HexagonTile[20][20];

    /**
     * Constructs a board.
     * @param store The store of the application.
     */
    public Board(Store<KBState> store) {
        this.store = store;
    }

    /**
     * Setup the GameBoard with textured hexagons.
     * @param group The pane on which the hexagons are drawn.
     * @param gameBoardData The data with all information.
     * @param resource The ResourceBundle for translating text.
     */
    public void setupGameBoard(Group group, TileType[][] gameBoardData, ResourceBundle resource) {
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

                HexagonTile hexagonTile = placeTile(xPos, yPos,gameBoardData[x][y], resource);
                gameBoard[x][y] = hexagonTile;

                group.getChildren().add(hexagonTile);
            }
        }
    }

    /**
     * Defines how the tiles should look like.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param tileType The type for the Tile.
     * @param resource The language support.
     * @return The Tile.
     */
    public abstract HexagonTile placeTile(int x, int y, TileType tileType, ResourceBundle resource);

    public HexagonTile[][] getGameBoard() {
        return gameBoard;
    }
}
