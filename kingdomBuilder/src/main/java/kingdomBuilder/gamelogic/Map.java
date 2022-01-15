package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static kingdomBuilder.gamelogic.Game.*;


/**
 * Contains the data of a map.
 */
class MapReadOnly {

    /**
     * Represents the tiles of the map in a 1D array.
     */
    protected final Tile[] tiles;

    /**
     * Represents the width of a quadrant.
     */
    public final int quadrantWidth;

    /**
     * Represents the width of the map.
     */
    public final int mapWidth;

    /**
     * The amount of tokens each special place should contain at the start of the game.
     */
    public final int startingTokenCount;

    /**
     * Creates the map from the given quadrants.
     *
     * @param startingTokenCount The amount of tokens that a special place contains at game start.
     * @param topLeft     The first quadrant in the top left.
     * @param topRight    The second quadrant in the top right.
     * @param bottomLeft  The third quadrant in the bottom left.
     * @param bottomRight The fourth quadrant in the bottom right.
     * @throws InvalidParameterException Throws an InvalidParameterException when the sizes between
     *                                   quadrants are not the same or if quadrant is not a square.
     */
    protected MapReadOnly(int startingTokenCount,
               TileType topLeft[],
               TileType topRight[],
               TileType bottomLeft[],
               TileType bottomRight[])
            throws InvalidParameterException {

        this.startingTokenCount = startingTokenCount;

        // create map
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
                        new Tile(x, y, topLeft[y * quadrantWidth + x], startingTokenCount);
            }
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexTopRight(x, y, quadrantWidth)] =
                        new Tile(x, y, topRight[y * quadrantWidth + x], startingTokenCount);
            }
        }

        // combines bottom left and bottom right into tiles array
        for (int y = 0; y < quadrantWidth; y++) {
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomLeft(x, y, quadrantWidth)] =
                        new Tile(x, y, bottomLeft[y * quadrantWidth + x], startingTokenCount);
            }
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomRight(x, y, quadrantWidth)] =
                        new Tile(x, y, bottomRight[y * quadrantWidth + x], startingTokenCount);
            }
        }
    }

    /**
     * Get the position of a 1D array from a 2D index.
     *
     * @param x     The x position of the 2D array.
     * @param y     The y position of the 2D array.
     * @param width The height/width of the 2D array.
     * @return The position in the 1D array.
     */
    protected static int to1DIndex(int x, int y, int width) {
        return y * width + x;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the top left quadrant.
     *
     * @param x     The x position of the 2D array.
     * @param y     The y position of the 2D array.
     * @param width The height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    protected static int to1DIndexTopLeft(int x, int y, int width) {
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
    protected static int to1DIndexTopRight(int x, int y, int width) {
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
    protected static int to1DIndexBottomLeft(int x, int y, int width) {
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
    protected static int to1DIndexBottomRight(int x, int y, int width) {
        return width * 2 * width + y * 2 * width + x + width;
    }

    /**
     * Calculates the x position for the tile that lies top left from the tile with the given coordinates.
     *
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies top left from the original tile.
     */
    protected static int topLeftX(int x, int y) {
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
     *
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies top right from the original tile.
     */
    protected static int topRightX(int x, int y) {
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
     *
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom left from the original tile.
     */
    protected int bottomLeftX(int x, int y) {
        return topLeftX(x, y);
    }

    /**
     * Calculates the x position for the tile that lies bottom right from the tile with the given coordinates.
     *
     * @param x The x coordinate of the original tile.
     * @param y The y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom right from the original tile.
     */
    protected int bottomRightX(int x, int y) {
        return topRightX(x, y);
    }

    /**
     * Gets the tile at the given index.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The tile at the given index.
     */
    public TileReadOnly at(int x, int y) {
        //TODO: make package default accessible.
        if (!isTileWithinBounds(x, y))
            throw new IndexOutOfBoundsException();

        return tiles[to1DIndex(x, y, mapWidth)];
    }

    /**
     * Checks if the given tile coordinates are within the boundaries of the map.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return Whether the coordinates are within the boundaries of the map.
     */
    public boolean isTileWithinBounds(int x, int y) {
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
        return (x == 0 || y == 0 || x == mapWidth - 1 || y == mapWidth - 1);
    }

    /**
     * Checks whether the tile type at given index is placeable excluding water which is only placeable with a token.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return Whether the tile is placeable.
     */
    public boolean isTilePlaceable(int x, int y) {

        TileReadOnly target = at(x, y);

        if (tokenType.contains(target.tileType))
            return false;
        else if (target.isOccupied())
            return false;
        else if (nonPlaceableTileTypes.contains(target.tileType))
            return false;

        return true;
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

    /**
     * Checks if two tiles have the same type.
     *
     * @param firstX   The horizontal position of the first tile.
     * @param firstY   The vertical position of the first tile.
     * @param secondX  The horizontal position of the second tile.
     * @param secondY  The vertical position of the second tile.
     * @return
     */
    public boolean tilesAreSameType(int firstX, int firstY, int secondX, int secondY) {
        return (at(firstX, firstY).tileType == at(secondX, secondY).tileType);
    }
}

/**
 * Contains the data of a map.
 */
public class Map extends MapReadOnly {

    /**
     * Creates the map from the given quadrants.
     *
     * @param startingTokenCount The amount of tokens that a special place contains at game start.
     * @param topLeft            The first quadrant in the top left.
     * @param topRight           The second quadrant in the top right.
     * @param bottomLeft         The third quadrant in the bottom left.
     * @param bottomRight        The fourth quadrant in the bottom right.
     * @throws InvalidParameterException Throws an InvalidParameterException when the sizes between
     *                                   quadrants are not the same or if quadrant is not a square.
     */
    public Map(int startingTokenCount,
                  TileType[] topLeft,
                  TileType[] topRight,
                  TileType[] bottomLeft,
                  TileType[] bottomRight)
            throws InvalidParameterException {
        super(startingTokenCount, topLeft, topRight, bottomLeft, bottomRight);
    }

    /**
     * Gets the tile at the given index.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The tile at the given index.
     */
    @Override
    public Tile at(int x, int y) {
        //TODO: make package default accessible.
        if (!isTileWithinBounds(x, y))
            throw new IndexOutOfBoundsException();

        return tiles[to1DIndex(x, y, mapWidth)];
    }

    /**
     * Places a settlement on a tile.
     *
     * @param x      The x coordinate of the tile.
     * @param y      The y coordinate of the tile.
     * @param player The player that places a settlement.
     */
    public void placeSettlement(Player player, int x, int y) {
        at(x, y).placeSettlement(player);
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
        Player player = at(fromX, fromY).removeSettlement();
        placeSettlement(player, toX, toY);
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
        return at(x, y).takeTokenFromSpecialPlace();
    }

    /**
     * Gets the width of the map.
     *
     * @return The width of the map.
     */
    public int getMapWidth() {
        return mapWidth;
    }

    /**
     * Returns an iterator that contains all surrounding tiles from a given hexagon.
     *
     * @param x The x coordinate of the Tile.
     * @param y The y coordinate of the Tile.
     * @return All surrounding Tiles.
     */
    public Iterator<Tile> surroundingTilesIterator(int x, int y) {

        return new Iterator<Tile>() {
            int state = -1;

            @Override
            public boolean hasNext() {
                switch (state) {
                    case -1:
                        // check given coordinates
                        if (!isTileWithinBounds(x, y)) {
                            return false;
                        }
                    case 0:
                        // top left
                        if (topLeftX(x, y) >= 0 && y > 0) {
                            state = 0;
                            return true;
                        }
                    case 1:
                        // top right
                        if (topRightX(x, y) < mapWidth && y > 0) {
                            state = 1;
                            return true;
                        }
                    case 2:
                        // left
                        if (x - 1 >= 0) {
                            state = 2;
                            return true;
                        }
                    case 3:
                        // right
                        if (x + 1 >= mapWidth) {
                            state = 3;
                            return true;
                        }
                    case 4:
                        // bottom left
                        if (bottomLeftX(x, y) >= 0 && y <= mapWidth) {
                            state = 4;
                            return true;
                        }
                    case 5:
                        // bottom right
                        if (bottomRightX(x, y) < mapWidth && y <= mapWidth) {
                            state = 5;
                            return true;
                        }
                    default:
                        // all tiles checked
                        return false;
                }
            }

            @Override
            public Tile next() {
                switch (state) {
                    case 0: state++; return at(topLeftX(x, y), y - 1);
                    case 1: state++; return at(topRightX(x, y), y - 1);
                    case 2: state++; return at(x - 1, y);
                    case 3: state++; return at(x + 1, y);
                    case 4: state++; return at(bottomLeftX(x, y), y + 1);
                    case 5: state++; return at(bottomRightX(x, y), y + 1);
                    default: return null;
                }
            }
        };
    }

    /**
     * Get all surrounding tiles of a given target tile.
     *
     * @param x The x coordinate of the target tile.
     * @param y The y coordinate of the target tile.
     * @return All tiles that surrounding target tile.
     */
    public Set<Tile> surroundingTiles(int x, int y) {
        if (!isTileWithinBounds(x, y)) {
            return null;
        }

        Set<Tile> surroundingTiles = new HashSet<>();

        // top left
        if (topLeftX(x, y) >= 0 && y > 0)
            surroundingTiles.add(at(topLeftX(x, y), y - 1));

        // top right
        if (topRightX(x, y) < mapWidth && y > 0)
            surroundingTiles.add(at(topRightX(x, y), y - 1));

        // left
        if (x - 1 >= 0)
            surroundingTiles.add(at(x - 1, y));

        // right
        if (x + 1 >= mapWidth)
            surroundingTiles.add(at(x + 1, y));

        // bottom left
        if (bottomLeftX(x, y) >= 0 && y <= mapWidth)
            surroundingTiles.add(at(bottomLeftX(x, y), y + 1));

        // bottom right
        if (bottomRightX(x, y) < mapWidth && y <= mapWidth)
            surroundingTiles.add(at(bottomRightX(x, y), y + 1));

        return surroundingTiles;
    }

    /**
     * Check if next to the tile is a token and return it.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The Token that is next to the settlement or null if there is no token;
     */
    public TileType specialPlaceInSurrounding(int x, int y) {
        Iterator<Tile> surroundingTiles = surroundingTiles(x, y).iterator();

        while (surroundingTiles.hasNext()) {
            Tile position = surroundingTiles.next();

            if (tokenType.contains(at(position.x, position.y).tileType)) {
                return at(position.x, position.y).tileType;
            }
        }
        return null;
    }

    /**
     * Checks if the given tile has a token left.
     *
     * @param x The x coordinate of the Tile.
     * @param y The y coordinate of the Tile.
     * @return True if tile has tokens left. False otherwise.
     */
    public boolean tileHasTokenLeft(int x, int y) {
        Tile tile = at(x, y);

        if (!tokenType.contains(tile.tileType))
            throw new InvalidParameterException("not a token");

        if (tile.hasTokens())
            return true;

        return false;
    }

    /**
     * Checks whether there is another settlement of the player on surrounding tiles.
     *
     * @param player The player that to check.
     * @param x      The x coordinate of the Tile.
     * @param y      The x coordinate of the Tile.
     * @return True if the player has another settlement on surrounding tile. False otherwise.
     */
    public boolean settlementOfPlayerOnSurroundingTiles(Player player, int x, int y) {
        Iterator<Tile> surrounding = surroundingTiles(x, y).iterator();
        Tile current;

        while (surrounding.hasNext()) {
            current = surrounding.next();
            if (at(current.x, current.y).isOccupiedByPlayer(player))
                return true;
        }
        return false;
    }

    /**
     * Check if there is at least one settlement of a Player on a terrain.
     *
     * @param player  The player to check.
     * @param terrain The terrain to check.
     * @return True if there is at least one settlement on terrain.
     */
    public boolean settlementOfPlayerOnTerrain(Player player, TileType terrain) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a terrain!");

        Iterator<Tile> terrainIterator = getEntireTerrain(terrain).iterator();
        Tile current;

        while (terrainIterator.hasNext()) {
            current = terrainIterator.next();

            if (at(current.x, current.y).occupiedBy == player)
                return true;
        }

        return false;
    }

    /**
     * Gets the entire terrain of a type.
     *
     * @param terrain The terrain to filter.
     * @return All tiles of a type.
     */
    public Set<Tile> getEntireTerrain(TileType terrain) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Tile> entireTerrain = new HashSet<>();

        for (int y = 0; y < mapWidth; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (at(x, y).tileType == terrain) {
                    entireTerrain.add(at(x, y));
                }
            }
        }
        return entireTerrain;
    }

    /**
     * Get only the free tiles of a terrain.
     *
     * @param terrain
     * @return
     */
    public Set<Tile> freeTilesOnTerrain(TileType terrain) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Tile> freeTiles = new HashSet<>();
        Iterator<Tile> terrainIterator = getEntireTerrain(terrain).iterator();
        Tile current;

        while (terrainIterator.hasNext()) {
            current = terrainIterator.next();

            if (isTilePlaceable(current.x, current.y))
                freeTiles.add(current);
        }

        return freeTiles;
    }

    /**
     * Get all free tiles at the border of the map.
     *
     * @param player
     * @return
     */
    public Set<Tile> freeTilesOnMapBorder(Player player) {
        Set<Tile> freeTiles = new HashSet<>();

        for (int i = 0; i < mapWidth; i++) {

            if (isTilePlaceable(i, 0))
                freeTiles.add(at(i, 0));

            if (isTilePlaceable(0, i))
                freeTiles.add(at(0, i));

            if (isTilePlaceable(i, mapWidth - 1))
                freeTiles.add(at(i, mapWidth - 1));

            if (isTilePlaceable(mapWidth - 1, i))
                freeTiles.add(at(mapWidth - 1, i));
        }
        return freeTiles;
    }

    /**
     * Checks if there is only one settlement of a player next to a special place.
     *
     * @param player The player to check-
     * @param x      The x position of the special place.
     * @param y      The y position of the special place.
     * @return
     */
    public boolean playerHasOnlyOneSettlementNextToSpecialPlace(Player player, int x, int y) {
        Iterator<Tile> tilesIterator = surroundingTiles(x, y).iterator();
        Tile token;
        int counter = 0;

        while (tilesIterator.hasNext()) {
            token = tilesIterator.next();
            if (at(token.x, token.y).occupiedBy() == player)
                counter++;
        }

        return counter == 1;
    }

    /**
     * Get all positions where the player has a settlement.
     *
     * @param player
     * @return
     */
    public Set<Tile> allSettlementsOfPlayerOnMap(Player player) {
        Set<Tile> allSettlements = new HashSet<>();

        for (int y = 0; y < mapWidth; y++)
            for (int x = 0; x < mapWidth; x++) {
                if (at(x, y).occupiedBy == player)
                    allSettlements.add(at(x, y));
            }
        return allSettlements;
    }
}


