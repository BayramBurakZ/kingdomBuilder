package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;
import java.util.Iterator;

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
    private int quadrantWidth;

    /**
     * Represents the width of the map.
     */
    private int mapWidth;

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

        quadrantWidth = (int) roundedWith;
        mapWidth = 2 * quadrantWidth;
        tiles = new Tile[topLeft.length * 4];

        // combines top left and top right into tiles array
        for (int y = 0; y < quadrantWidth; y++) {
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexTopLeft(x, y, quadrantWidth)] =
                        new Tile(topLeft[y * quadrantWidth + x], startingTokenCount);
            }
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexTopRight(x, y, quadrantWidth)] =
                        new Tile(topRight[y * quadrantWidth + x], startingTokenCount);
            }
        }

        // combines bottom left and bottom right into tiles array
        for (int y = 0; y < quadrantWidth; y++) {
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomLeft(x, y, quadrantWidth)] =
                        new Tile(bottomLeft[y * quadrantWidth + x], startingTokenCount);
            }
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomRight(x, y, quadrantWidth)] =
                        new Tile(bottomRight[y * quadrantWidth + x], startingTokenCount);
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
     * @throws UnsupportedOperationException when tile is not a special place.
     */
    public TileType takeToken(int x, int y) throws HasNoTokenException {
        return getTile(x, y).takeTokenFromSpecialPlace();
    }

    /**
     * Get the position of a 1D array from a 2D index.
     *
     * @param x     The x position of the 2D array.
     * @param y     The y position of the 2D array.
     * @param width The height/width of the 2D array.
     * @return The position in the 1D array.
     */
    private int to1DIndex(int x, int y, int width) {
        return y * width + x;
    }

    //TODO: eliminate width from return
    /**
     * Get the position of the 1D game map from a 2D index for the top left quadrant.
     *
     * @param x     The x position of the 2D array.
     * @param y     The y position of the 2D array.
     * @param width The height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    private int to1DIndexTopLeft(int x, int y, int width) {
        return y * 2 * width + x;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the top right quadrant.
     *
     * @param x     The x position of the 2D array.
     * @param y     The y position of the 2D array.
     * @param width The height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    private int to1DIndexTopRight(int x, int y, int width) {
        return y * 2 * width + x + width;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the bottom left quadrant.
     *
     * @param x     The x position of the 2D array.
     * @param y     The y position of the 2D array.
     * @param width The height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    private int to1DIndexBottomLeft(int x, int y, int width) {
        return width * 2 * width + y * 2 * width + x;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the bottom right quadrant.
     *
     * @param x     The x position of the 2D array.
     * @param y     The y position of the 2D array.
     * @param width The height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    private int to1DIndexBottomRight(int x, int y, int width) {
        return width * 2 * width + y * 2 * width + x + width;
    }

    /**
     * The tile at the given index.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The tile at the index.
     */
    private Tile getTile(int x, int y) {
        //TODO: make package default accessible.
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapWidth)
            throw new IndexOutOfBoundsException();

        return tiles[to1DIndex(x, y, mapWidth)];
    }

    /**
     * Get the tile type at the given index.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The tile type of the tile.
     */
    public TileType getTileType(int x, int y) {
        //TODO: make package default accessible.
        return getTile(x, y).getTileType();
    }

    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 || y >= 0 || x < mapWidth || y < mapWidth);
    }

    /**
     * Check whether the tile at given index is at the border of the map.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return Whether the tile is at the border.
     */
    public boolean isTileAtBorder(int x, int y) {

        if (x == 0 || y == 0 || x == mapWidth - 1 || y == mapWidth - 1)
            return true;

        return false;
    }

    /**
     * Checks whether the tile type at given index is placeable excluding water which is only placeable with a token.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return Whether the tile is placeable.
     */
    public boolean isTilePlaceable(int x, int y) {

        Tile target = getTile(x, y);

        if (tokenType.contains(target.getTileType()))
            return false;
        else if (target.isOccupied())
            return false;
        else if (nonPlaceableTileTypes.contains(target.getTileType()))
            return false;

        return true;
    }

    /**
     * Calculates the x position for the tile that lies top left from the tile with the given coordinates.
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies top left from the original tile.
     */
    public int topLeftX(int x, int y) {
        if (y % 2 == 0) {
            // even row
            return x - 1;
        } else {
            // odd row
            return x;
        }
    }

    /**
     * Calculates the x position for the tile that lies top right from the tile with the given coordinates.
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies top right from the original tile.
     */
    public int topRightX(int x, int y) {
        if (y % 2 == 0) {
            // even row
            return x;
        } else {
            // odd row
            return x + 1;
        }
    }

    /**
     * Calculates the x position for the tile that lies bottom left from the tile with the given coordinates.
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom left from the original tile.
     */
    public int bottomLeftX(int x, int y) {
        return topLeftX(x, y);
    }

    /**
     * Calculates the x position for the tile that lies bottom right from the tile with the given coordinates.
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom right from the original tile.
     */
    public int bottomRightX(int x, int y) {
        return topRightX(x, y);
    }

    /**
     * Checks if two tiles are neighbours in the hexagon map.
     *
     * @param firstX  The x coordinate of the first tile.
     * @param firstY  The y coordinate of the first tile.
     * @param secondX The x coordinate of the second tile.
     * @param secondY The y coordinate of the second tile.
     * @return Whether both tiles are neighbors.
     * @throws InvalidParameterException When either of the tile coordinates are out of bounds.
     */
    public boolean areNeighbouringTiles(int firstX, int firstY, int secondX, int secondY)
            throws InvalidParameterException {

        if (firstX < 0 || firstY < 0 || firstX >= mapWidth || firstY >= mapWidth)
            throw new InvalidParameterException("First tile coordinates are out of bounds with x:" + firstX +
                    " y:" + firstY);
        if (secondX < 0 || secondY < 0 || secondX >= mapWidth || secondY >= mapWidth)
            throw new InvalidParameterException("Second tile coordinates are out of bounds with x:" + secondX +
                    " y:" + secondY);

        // Compare Y
        if (Math.abs(firstY - secondY) > 1)
            return false;

        // Neighbour in same row
        if (firstY == secondY && Math.abs(firstX - secondX) > 1)
            return false;

        if (firstY % 2 == 0) {
            // even row

            // top/bottom left or top/bottom right
            if (firstX == secondX - 1 || firstX == secondX)
                return true;

        } else {
            // odd row

            // top/bottom right or top/bottom left
            if (firstX == secondX + 1 || firstX == secondX)
                return true;
        }

        return false;
    }

    // TODO: JavaDoc
    /**
     * Checks if two tiles have the same type.
     *
     * @param firstX
     * @param firstY
     * @param secondX
     * @param secondY
     * @return
     */
    public boolean tilesAreSameType(int firstX, int firstY, int secondX, int secondY) {
        if (getTile(firstX, firstY).getTileType() == getTile(secondX, secondY).getTileType())
            return true;

        return false;
    }

    // TODO: JavaDoc
    public Player occupiedBy(int x, int y) {
        return getTile(x, y).occupiedBy();
    }

    // TODO: JavaDoc
    public boolean isOccupied(int x, int y) {
        return getTile(x, y).isOccupied();
    }

    // TODO: JavaDoc
    public int getMapWidth(){
        return mapWidth;
    }

    private Iterator<Position> surroundingTiles(int x, int y) {
        return new Iterator<Position>() {
            int state = -1;
            @Override
            public boolean hasNext() {
                switch (state) {
                    case -1:
                        // check given coordinates
                        if (!isWithinBounds(x, y)) {
                            return false;
                        }
                    case 0:
                        // top left
                        state = 0;
                        if (topLeftX(x, y) >= 0 && y > 0) {
                            return true;
                        }
                    case 1:
                        // top right
                        state = 1;
                        if (topRightX(x,y) < mapWidth && y > 0) {
                            return true;
                        }
                    case 2:
                        // left
                        state = 2;
                        if ( x - 1 >= 0) {
                            return true;
                        }
                    case 3:
                        // right
                        state = 3;
                        if ( x + 1 >= mapWidth) {
                            return true;
                        }
                    case 4:
                        // bottom left
                        state = 4;
                        if (bottomLeftX(x, y) >= 0 && y <= mapWidth) {
                            return true;
                        }
                    case 5:
                        // bottom right
                        state = 5;
                        if (bottomRightX(x, y) < mapWidth && y <= mapWidth) {
                            return true;
                        }
                    default:
                        // all tiles checked
                        return false;
                }
            }

            @Override
            public Position next() {
                return switch (state) {
                    case 0 -> new Position(topLeftX(x, y), y - 1);
                    case 1 -> new Position(topRightX(x, y), y - 1);
                    case 2 -> new Position(x - 1, y);
                    case 3 -> new Position(x + 1, y);
                    case 4 -> new Position(bottomLeftX(x, y),y + 1);
                    case 5 -> new Position(bottomRightX(x, y), y + 1);
                    default -> null;
                };
            }
        };
    }
}
