package kingdomBuilder.gui.gameboard;

import javafx.scene.Group;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.game.ClientTurnAction;
import kingdomBuilder.gamelogic.*;
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
     * Represents the store of the application.
     */
    private Store<KBState> store;

    /**
     * Represents the hexagon, that is marked to move.
     */
    private HexagonTile markedHexagon;

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


    /**
     * Marks the hexagon and updates the highlight for the selected token. If the token is null, nothing will be
     * highlighted.
     * @param hexagon the hexagon to be marked.
     */
    public void markHexagonToMove(HexagonTile hexagon) {
        markedHexagon = hexagon;
        Game game = store.getState().game;
        if (store.getState().token == null)
            return;

        switch (store.getState().token) {
            case BARN -> highlightTerrain(game.allTokenBarnTiles(game.currentPlayer, true));
            case HARBOR -> highlightTerrain(game.allTokenHarborTiles(game.currentPlayer, true));
            case PADDOCK -> highlightTerrain(
                    game.allTokenPaddockTiles(game.currentPlayer, markedHexagon.getX(), markedHexagon.getY()));
        }
    }

    /**
     * triggered when the user clicks on a hexagon.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
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

    /**
     * sends the final turn message to the reducer
     * @param id the id of the player.
     * @param x the x-coordinate to place a settlement.
     * @param y the y-coordinate to place a settlement.
     * @param toX the x-coordinate to move a settlement to.
     * @param toY the y-coordinate to move a settlement to.
     * @param isToken if the turn is a token.
     * @param isMove if the turn is a turn to move a settlement.
     */
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
    public void placeSettlement(int x, int y, PlayerColor color) {
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
