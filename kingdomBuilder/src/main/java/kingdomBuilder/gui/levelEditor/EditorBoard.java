package kingdomBuilder.gui.levelEditor;

import javafx.scene.Group;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.gui.base.Board;

import java.util.ResourceBundle;

/**
 * Class that contains the Board for the level editor.
 */
public class EditorBoard extends Board {

    /**
     * Represents the width of one quadrant.
     */
    private static final int SIZE = 10;

    /**
     * Represents the EditorController for access.
     */
    private LevelEditorController editorController;

    /**
     * Represents the board that contains the hexagons.
     */
    protected EditorTile[][] board = new EditorTile[SIZE][SIZE];

    /**
     * Constructs a board.
     *
     * @param editorController the LevelEditor Controller
     */
    public EditorBoard(LevelEditorController editorController) {
        this.editorController = editorController;
    }

    /**
     * Places an EditorTile on the board.
     *
     * @param group the group where the element is added.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param xPos the x-coordinate for the element.
     * @param yPos the x-coordinate for the element.
     * @param tileType the type for the tile.
     * @param resource the language support.
     */
    @Override
    public void placeTileOnBoard(Group group, int x, int y, int xPos, int yPos,
                                 TileType tileType, ResourceBundle resource) {
        EditorTile editorTile = new EditorTile(x, y, xPos, yPos, null, resource, editorController);
        board[x][y] = editorTile;

        group.getChildren().add(editorTile);
    }

    /**
     * Creates a quadrant with empty tiles.
     * @param group the group in which the Tiles should be added.
     * @param resource the language support.
     */
    public void setupEmptyQuadrant(Group group, ResourceBundle resource) {
        TileType[][] empty = new TileType[SIZE][SIZE];
        setupBoard(group, empty, resource);
    }

    /**
     * Gets the current board.
     *
     * @return The board.
     */
    public EditorTile[][] getBoard() {
        return board;
    }
}
