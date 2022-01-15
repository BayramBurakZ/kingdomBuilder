package kingdomBuilder.gui.gameboard;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.gui.base.Board;
import kingdomBuilder.model.TileType;
import kingdomBuilder.redux.Store;

import java.util.ResourceBundle;

/**
 * This class is used to contain all functions in terms of the boards gui.
 */
public class GameBoard extends Board {

    /**
     * Represents the width of the board for a game.
     */
    private static final int SIZE = 20;
    /**
     * Represents the board that contains the hexagons.
     */
    // TODO: use constants from Datalogic
    protected HexagonTile[][] board = new HexagonTile[SIZE][SIZE];

    /**
     * Constructor to instantiate the GameBoard.
     * @param store The Store to access the state.
     */
    public GameBoard(Store<KBState> store) {
        // TODO: make constant
        super(store);
        // TODO: Subscribers:
        //  - Token Enable/Disable -> highlightTerrain()
        //  - Settlements from others -> updateBoard()
    }

    /**
     * Places a tile on the board.
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
    public void placeTileOnBoard(Group group,int x, int y, int xPos, int yPos,
                                 TileType tileType, ResourceBundle resource) {
        HexagonTile hexagonTile = new HexagonTile(xPos, yPos, tileType, resource);
        board[x][y] = hexagonTile;

        group.getChildren().add(hexagonTile);
    }


    /**
     * Highlights Hexagons on the map which matches the given type.
     */
    public void highlightTerrain() {
        // TODO: remove randomizer
        int random = (int) (Math.random() * 5) + 1;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                // //TODO: Change to Gamelogic
                if (board[x][y].getTileType() == TileType.valueOf(random)) {
                    if (!board[x][y].isElevated())
                        board[x][y].setElevated();
                } else {
                    if (board[x][y].isElevated())
                        board[x][y].removeElevated();
                }
            }
        }
    }

    /**
     * Updates the Board according to the state.
     */
    private void updateBoard() {
        //TODO: implement
    }

    /**
     * Gets the board.
     * @return The Board.
     */
    public HexagonTile[][] getBoard() {
        return board;
    }
}
