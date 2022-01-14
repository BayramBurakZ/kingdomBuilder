package kingdomBuilder.gui.gameboard;

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
     * Constructor to instantiate the GameBoard.
     * @param store The Store to access the state.
     */
    public GameBoard(Store<KBState> store) {
        super(store);
        // TODO: Subscribers:
        //  - Token Enable/Disable -> highlightTerrain()
        //  - Settlements from others -> updateBoard()
    }

    @Override
    public HexagonTile placeTile(int x, int y, TileType tileType, ResourceBundle resource) {
        return new HexagonTile(x, y, tileType, resource);
    }


    /**
     * Highlights Hexagons on the map which matches the given type.
     */
    public void highlightTerrain() {
        // TODO: remove randomizer
        int random = (int) (Math.random() * 5) + 1;
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard.length; x++) {
                // //TODO: Change to Gamelogic
                if (gameBoard[x][y].getTileType() == TileType.valueOf(random)) {
                    if (!gameBoard[x][y].isElevated())
                        gameBoard[x][y].setElevated();
                } else {
                    if (gameBoard[x][y].isElevated())
                        gameBoard[x][y].removeElevated();
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
}
