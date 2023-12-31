package kingdomBuilder.gui.base;

import javafx.scene.Group;
import kingdomBuilder.gamelogic.GameMap;
import kingdomBuilder.gamelogic.TileType;

import java.util.ResourceBundle;

/**
 * Abstract class to generate a board.
 */
public abstract class Board {

    /**
     * Represents the resourceBundle that used for language support.
     */
    protected static ResourceBundle resourceBundle;

    /**
     * Set the GameBoard with textured hexagons up.
     * @param group the pane on which the hexagons are drawn.
     * @param gameMap the map with all information.
     * @param resource the ResourceBundle for translating text.
     */
    public void setupBoard(Group group, GameMap gameMap, ResourceBundle resource) {
        int width = gameMap.mapWidth;

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                int xPos;
                if (y % 2 == 0) {
                    xPos = x * 70;
                } else {
                    xPos = x * 70 + 35;
                }

                int yPos = y * 60;
                placeTileOnBoard(group, x, y, xPos, yPos, gameMap.at(x, y).tileType, resource);
            }
        }
    }

    /**
     * Set the GameBoard with textured hexagons up.
     * @param group the pane on which the hexagons are drawn.
     * @param map the map with all information.
     * @param resource the ResourceBundle for translating text.
     */
    public void setupBoard(Group group, TileType[][] map, ResourceBundle resource) {
        int width = map.length;

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                int xPos;
                if (y % 2 == 0) {
                    xPos = x * 70;
                } else {
                    xPos = x * 70 + 35;
                }

                int yPos = y * 60;

                placeTileOnBoard(group, x, y, xPos, yPos, null, resource);
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
                                          int xPos, int yPos, TileType tileType, ResourceBundle resource);
}
