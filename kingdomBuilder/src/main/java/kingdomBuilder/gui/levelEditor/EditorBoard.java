package kingdomBuilder.gui.levelEditor;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.gui.base.Board;
import kingdomBuilder.model.TileType;
import kingdomBuilder.redux.Store;

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
     * @param store The store of the application.
     * @param editorController The LevelEditor Controller
     */
    public EditorBoard(Store<KBState> store, LevelEditorController editorController) {
        super(store);
        this.editorController = editorController;
    }

    /**
     * Places an EditorTile on the board.
     *
     * @param group The group where the element is added.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param xPos The x-coordinate for the element.
     * @param yPos The x-coordinate for the element.
     * @param tileType The type for the tile.
     * @param resource The language support.
     */
    @Override
    public void placeTileOnBoard(Group group, int x, int y, int xPos, int yPos,
                                 TileType tileType, ResourceBundle resource) {
        EditorTile editorTile = new EditorTile(xPos, yPos, null, resource, editorController);
        board[x][y] = editorTile;

        group.getChildren().add(editorTile);
    }

    /**
     * Creates a quadrant with empty tiles.
     * @param group The group in which the Tiles should be added.
     * @param resource The language support.
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
