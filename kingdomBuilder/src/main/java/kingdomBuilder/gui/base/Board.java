package kingdomBuilder.gui.base;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.gamelogic.Game;
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
    protected static ResourceBundle resourceBundle;

    /**
     * Constructs a board.
     * @param store the store of the application.
     */
    public Board(Store<KBState> store) {
        this.store = store;
    }

    /**
     * Set the GameBoard with textured hexagons up.
     * @param group the pane on which the hexagons are drawn.
     * @param gameBoardData the data with all information.
     * @param resource the ResourceBundle for translating text.
     */
    public void setupBoard(Group group, Game.TileType[][] gameBoardData, ResourceBundle resource) {
        int width = gameBoardData.length;

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                int xPos;
                if (y % 2 == 0) {
                    xPos = x * 70;
                } else {
                    xPos = x * 70 + 35;
                }

                int yPos = y * 60;

                placeTileOnBoard(group, x, y, xPos, yPos, gameBoardData[x][y], resource);
            }
        }
    }

    /**
     * Places a tile on the board.
     *
     * @param group the group where the element is added.
     * @param xPos the x-coordinate for the element.
     * @param yPos the x-coordinate for the element.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param tileType the type for the tile.
     * @param resource the language support.
     */
    public abstract void placeTileOnBoard(Group group, int x, int y,
                                          int xPos, int yPos, Game.TileType tileType, ResourceBundle resource);
}
