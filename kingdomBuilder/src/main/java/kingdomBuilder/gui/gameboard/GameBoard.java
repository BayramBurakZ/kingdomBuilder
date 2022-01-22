package kingdomBuilder.gui.gameboard;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gamelogic.Game.TileType;
import kingdomBuilder.gui.base.Board;
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
    protected HexagonTile[][] board = new HexagonTile[SIZE][SIZE];

    /**
     * Constructor to instantiate the GameBoard.
     * @param store the Store to access the state.
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
        HexagonTile hexagonTile = new HexagonTile(xPos, yPos, tileType, resource);
        board[x][y] = hexagonTile;

        group.getChildren().add(hexagonTile);
    }

    /**
     * Highlights Hexagons on the map which matches the given type.
     */
    public void highlightTerrain() {
        // TODO: remove randomizer
        int random = (int) (Math.random() * Game.placeableTileTypes.size());
        TileType tileType = (TileType) Game.placeableTileTypes.toArray()[random];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                // //TODO: Change to Gamelogic
                if (board[x][y].getTileType() == tileType) {
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
