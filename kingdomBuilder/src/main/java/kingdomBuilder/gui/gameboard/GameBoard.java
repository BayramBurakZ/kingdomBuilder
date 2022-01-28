package kingdomBuilder.gui.gameboard;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gamelogic.Game.TileType;
import kingdomBuilder.gamelogic.Tile;
import kingdomBuilder.gui.base.Board;
import kingdomBuilder.redux.Store;

import java.util.ResourceBundle;
import java.util.Set;

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
     *
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
     * @param set the tiles to highlight
     */
    public void highlightTerrain(Set<Tile> set) {
        for (HexagonTile[] o : board)
            for (HexagonTile h : o) {
                h.removeElevated();
            }
        for (Tile t : set) {
            board[t.x][t.y].setElevated();
        }
    }

    /**
     * Updates the Board according to the state.
     */
    private void updateBoard() {
        //TODO: implement
    }

    /**
     * Sets a settlement in the gui.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param color the player color.
     */
    public void placeSettlement(int x, int y, Game.PlayerColor color) {
        board[x][y].placeSettlement(color);
    }

    /**
     * Removes a settlement in the gui.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public void removeSettlement(int x, int y) {
        board[x][y].removeSettlement();
    }

    /**
     * Gets the board.
     *
     * @return The Board.
     */
    public HexagonTile[][] getBoard() {
        return board;
    }
}
