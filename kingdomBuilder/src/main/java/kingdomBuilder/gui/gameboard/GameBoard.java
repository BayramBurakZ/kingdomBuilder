package kingdomBuilder.gui.gameboard;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.game.ClientTurnAction;
import kingdomBuilder.gamelogic.ClientTurn;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gamelogic.Game.TileType;
import kingdomBuilder.gamelogic.Tile;
import kingdomBuilder.gui.base.Board;
import kingdomBuilder.gui.controller.GameViewController;
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

    private Store<KBState> store;

    /**
     * Constructor to instantiate the GameBoard.
     *
     * @param store the Store to access the state.
     */
    public GameBoard(Store store) {
        this.store = store;
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
        HexagonTile hexagonTile = new HexagonTile(xPos, yPos, x, y, tileType, resource, this);
        board[x][y] = hexagonTile;

        group.getChildren().add(hexagonTile);
    }

    // the marked hexagon
    private HexagonTile markedHexagon;

    // marks the hexagon and updates the highlight
    public void markHexagonToMove(HexagonTile hexagon) {
        markedHexagon = hexagon;
        Game game = store.getState().game;

        switch (store.getState().token) {
            case BARN -> highlightTerrain(game.allTokenBarnTiles(game.currentPlayer, true));
            case HARBOR -> highlightTerrain(game.allTokenHarborTiles(game.currentPlayer, true));
            case PADDOCK -> highlightTerrain(
                    game.allTokenPaddockTiles(game.currentPlayer, markedHexagon.getX(), markedHexagon.getY()));
        }
    }

    //triggered when the user clicks on a hexagon
    public void hexagonClicked(int x, int y) {
        int playerID = store.getState().client.getClientId();
        TileType token = store.getState().token;

        if (token == null) {
            sendClientTurn(playerID, x, y, -1, -1, false, false);
            return;
        }

        if (token == TileType.BARN || token == TileType.HARBOR || token == TileType.PADDOCK) {
            // TOKEN WITH MOVE
            if (markedHexagon == null) {
                // first hexagon
                markHexagonToMove(board[x][y]);
                markedHexagon.setMarker();
            } else {
                // second hexagon
                sendClientTurn(playerID, markedHexagon.getX(), markedHexagon.getY(), x, y, true, true);
                markedHexagon.removeMarker();
                markedHexagon = null;
            }

        } else {
            // TOKEN ONLY PLACE
            sendClientTurn(playerID, x, y, -1, -1, true, false);
        }
    }

    // sends the final turn message to the reducer
    public void sendClientTurn(int id, int x, int y, int toX, int toY, boolean isToken, boolean isMove) {

        ClientTurn turn;
        if (!isToken) {
            // PLACING BASIC TURN
            turn = new ClientTurn(
                    store.getState().client.getClientId(),
                    ClientTurn.TurnType.PLACE,
                    x,
                    y,
                    -1,
                    -1
            );
        } else if (!isMove) {
            // PLACING TOKEN
            turn = new ClientTurn(
                    store.getState().client.getClientId(),
                    ClientTurn.TurnType.valueOf(String.valueOf(store.getState().token)),
                    x,
                    y,
                    -1,
                    -1
            );
        } else {
            //MOVING TOKEN
            turn = new ClientTurn(
                    store.getState().client.getClientId(),
                    ClientTurn.TurnType.valueOf(String.valueOf(store.getState().token)),
                    x,
                    y,
                    toX,
                    toY
            );
        }

        store.dispatch(new ClientTurnAction(turn));
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
     * Sets a settlement in the gui.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param color the player color.
     */
    public void placeSettlement(int x, int y, Game.PlayerColor color) {
        board[y][x].placeSettlement(color);
    }

    /**
     * Removes a settlement in the gui.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public void removeSettlement(int x, int y) {
        board[y][x].removeSettlement();
    }

    /**
     * Gets the board.
     * @return the Board.
     */
    public HexagonTile[][] getBoard() {
        return board;
    }

    /**
     * Gets the marked hexagon.
     * @return the marked hexagon.
     */
    public HexagonTile getMarkedHexagon() {
        return markedHexagon;
    }
}
