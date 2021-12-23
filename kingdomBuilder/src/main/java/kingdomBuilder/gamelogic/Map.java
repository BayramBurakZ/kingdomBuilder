package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;

import static kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a map.
 */
public class Map {

    /**
     * Represents the tiles of the map in a 1D array.
     */
    private Tile tiles[];

    /**
     * Represents the width of a quadrant.
     */
    private int width;

    /**
     * Represents the amount of tokens that a special place contains at game start.
     */
    private int startingTokenCount;

    /**
     * Constructs the map.
     *
     * @param startingTokenCount The amount of tokens that a special place contains at game start.
     */
    public Map(int startingTokenCount) {
        this.startingTokenCount = startingTokenCount;
    }

    /**
     * Creates the map from the given quadrants and sets it internally.
     *
     * @param topLeft     The first quadrant in the top left.
     * @param topRight    The second quadrant in the top right.
     * @param bottomLeft  The third quadrant in the bottom left.
     * @param bottomRight The fourth quadrant in the bottom right.
     * @throws InvalidParameterException Throws an InvalidParameterException when the sizes between
     *                                   quadrants are not the same or if quadrant is not a square.
     */
    public void createMap(TileType topLeft[], TileType topRight[], TileType bottomLeft[], TileType bottomRight[])
            throws InvalidParameterException {

        if (topLeft.length != topRight.length || topLeft.length != bottomLeft.length
                || topLeft.length != bottomRight.length)
            throw new InvalidParameterException();

        double sqrt = Math.sqrt(topLeft.length);
        double roundedWith = Math.round(sqrt);

        if (sqrt != roundedWith) {
            throw new InvalidParameterException();
        }

        width = (int) roundedWith;
        tiles = new Tile[topLeft.length * 4];

        // combines top left and top right into tiles array
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                tiles[to1DIndexTopLeft(x, y, width)] = new Tile(topLeft[y * width + x], startingTokenCount);
            }
            for (int x = 0; x < width; x++) {
                tiles[to1DIndexTopRight(x, y, width)] = new Tile(topRight[y * width + x], startingTokenCount);
            }
        }

        // combines bottom left and bottom right into tiles array
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                tiles[to1DIndexBottomLeft(x, y, width)] = new Tile(bottomLeft[y * width + x], startingTokenCount);
            }
            for (int x = 0; x < width; x++) {
                tiles[to1DIndexBottomRight(x, y, width)] = new Tile(bottomRight[y * width + x], startingTokenCount);
            }
        }
    }

    /**
     * Places a settlement on a tile.
     *
     * @param x      The x coordinate of the tile.
     * @param y      The y coordinate of the tile.
     * @param player The player that places a settlement.
     */
    public void placeSettlement(int x, int y, Player player) {
        getTile(x, y).placeSettlement(player);
    }

    /**
     * Moves a settlement from one tile to another.
     *
     * @param fromX The x coordinate from the settlement that gets removed.
     * @param fromY The y coordinate from the settlement that gets removed.
     * @param toX   The x coordinate of the new settlement.
     * @param toY   The y coordinate of the new settlement.
     */
    public void moveSettlement(int fromX, int fromY, int toX, int toY) {
        Player player = getTile(fromX, fromY).removeSettlement();
        placeSettlement(toX, toY, player);
    }

    /**
     * Take a token from a special place.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The token.
     */
    public TokenType takeToken(int x, int y) {
        return getTile(x, y).takeTokenFromSpecialPlace();
    }

    /**
     * Get the position of a 2D array in a 1D array.
     *
     * @param x     The position of the 2D array.
     * @param y     The position of th 2D array.
     * @param width The width of the 2D array.
     * @return The position in a 1D array.
     */
    private int to1DIndex(int x, int y, int width) {
        return y * 2 * width + x;
    }

    private int to1DIndexTopLeft(int x, int y, int width) {
        return y * 2 * width + x;
    }

    private int to1DIndexTopRight(int x, int y, int width) {
        return y * 2 * width + x + width;
    }

    private int to1DIndexBottomLeft(int x, int y, int width) {
        return width * 2 * width + y * 2 * width + x;
    }

    private int to1DIndexBottomRight(int x, int y, int width) {
        return width * 2 * width + y * 2 * width + x + width;
    }

    /**
     * Get the tile at the given index.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile
     * @return The tile.
     */
    public Tile getTile(int x, int y) {
        //TODO: make package default accessible.
        return tiles[to1DIndex(x, y, width)];
    }
}
