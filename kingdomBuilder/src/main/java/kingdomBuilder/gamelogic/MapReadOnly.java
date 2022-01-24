package kingdomBuilder.gamelogic;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Contains the data of a map.
 */
public class MapReadOnly<T extends TileReadOnly> implements Iterable<T> {

    public static final int DEFAULT_STARTING_TOKEN_COUNT = 2;

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
     * @param startingTokenCount the amount of tokens that a special place contains at game start.
     * @param topLeft            the first quadrant in the top left.
     * @param topRight           the second quadrant in the top right.
     * @param bottomLeft         the third quadrant in the bottom left.
     * @param bottomRight        the fourth quadrant in the bottom right.
     * @throws InvalidParameterException Throws an InvalidParameterException when the sizes between
     *                                   quadrants are not the same or if quadrant is not a square.
     */
    public MapReadOnly(int startingTokenCount,
                       Game.TileType[] topLeft,
                       Game.TileType[] topRight,
                       Game.TileType[] bottomLeft,
                       Game.TileType[] bottomRight)
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
     * @param width the height/width of the 2D array.
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
     * @param width the height/width of the 2D quadrant.
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
     * @param width the height/width of the 2D quadrant.
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
     * @param width the height/width of the 2D quadrant.
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
     * @param width the height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    protected static int to1DIndexBottomRight(int x, int y, int width) {
        return width * 2 * width + y * 2 * width + x + width;
    }

    /**
     * Calculates the x position for the tile that lies top left from the tile with the given coordinates.
     *
     * @param x the x coordinate of the original tile.
     * @param y the y coordinate of the original tile.
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
     * @param x the x coordinate of the original tile.
     * @param y the y coordinate of the original tile.
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
     * @param x the x coordinate of the original tile.
     * @param y the y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom left from the original tile.
     */
    protected int bottomLeftX(int x, int y) {
        return topLeftX(x, y);
    }

    /**
     * Calculates the x position for the tile that lies bottom right from the tile with the given coordinates.
     *
     * @param x the x coordinate of the original tile.
     * @param y the y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom right from the original tile.
     */
    protected int bottomRightX(int x, int y) {
        return topRightX(x, y);
    }

    /**
     * Gets the tile at the given index.
     *
     * @param x the x coordinate of the tile.
     * @param y the y coordinate of the tile.
     * @return The tile at the given index.
     */
    public T at(int x, int y) {
        if (!isWithinBounds(x, y))
            throw new IndexOutOfBoundsException();

        return (T) tiles[to1DIndex(x, y, mapWidth)];
    }

    /**
     * Checks if the given tile coordinates are within the boundaries of the map.
     *
     * @param x the x coordinate of the tile.
     * @param y the y coordinate of the tile.
     * @return Whether the coordinates are within the boundaries of the map.
     */
    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 || y >= 0 || x < mapWidth || y < mapWidth);
    }

    /**
     * Returns an iterator that contains all surrounding tiles from a given hexagon.
     *
     * @param x the x coordinate of the Tile.
     * @param y the y coordinate of the Tile.
     * @return All surrounding Tiles.
     */
    public Iterator<T> surroundingTilesIterator(int x, int y) {

        return new Iterator<T>() {
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
            public T next() {
                switch (state) {
                    case 0:
                        state++;
                        return at(topLeftX(x, y), y - 1);
                    case 1:
                        state++;
                        return at(topRightX(x, y), y - 1);
                    case 2:
                        state++;
                        return at(x - 1, y);
                    case 3:
                        state++;
                        return at(x + 1, y);
                    case 4:
                        state++;
                        return at(bottomLeftX(x, y), y + 1);
                    case 5:
                        state++;
                        return at(bottomRightX(x, y), y + 1);
                    default:
                        return null;
                }
            }
        };
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Arrays.stream((T[]) tiles).iterator();
    }

    private class SurroundingTilesCollector implements Collector<T, Set<T>, Set<T>> {

        private final Set<T> originalTiles;

        private SurroundingTilesCollector(Set<T> tiles) {
            super();
            this.originalTiles = tiles;
        }

        @Override
        public Supplier<Set<T>> supplier() {
            return HashSet::new;
        }

        @Override
        public BiConsumer<Set<T>, T> accumulator() {
            return (set, tile) -> {
                for (var it = surroundingTilesIterator(tile.x, tile.y); it.hasNext(); ) {
                    var surroundingTile = it.next();
                    if (!originalTiles.contains(surroundingTile)) set.add(surroundingTile);
                }
            };
        }

        @Override
        public BinaryOperator<Set<T>> combiner() {
            return (set1, set2) -> {
                set1.addAll(set2);
                return set1;
            };
        }

        @Override
        public Function<Set<T>, Set<T>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
        }
    }

    public SurroundingTilesCollector toSurroundingTilesSet(Set<T> tiles) {
        return new SurroundingTilesCollector(tiles);
    }

    /**
     * Check if settlement of a player has at least one neighbour.
     *
     * @param player the player to check-
     * @param tile   Tile to look for.
     * @return True if player has a neighbouring settlement. False otherwise.
     */
    private boolean playerHasASettlementInSurrounding(Player player, T tile) {
        int x = tile.x;
        int y = tile.y;

        // right tile
        if (isWithinBounds(x + 1, y) && at(x + 1, y).isOccupiedByPlayer(player))
            return true;

        // left tile
        if (isWithinBounds(x - 1, y) && at(x - 1, y).isOccupiedByPlayer(player))
            return true;

        // top right
        if (isWithinBounds(topRightX(x, y), y + 1) && at(topRightX(x, y), y + 1).isOccupiedByPlayer(player))
            return true;

        // top left
        if (isWithinBounds(topLeftX(x, y), y + 1) && at(topLeftX(x, y), y + 1).isOccupiedByPlayer(player))
            return true;

        // bottom right
        if (isWithinBounds(bottomRightX(x, y), y - 1) && at(bottomRightX(x, y), y - 1).isOccupiedByPlayer(player))
            return true;

        // bottom left
        if (isWithinBounds(bottomLeftX(x, y), y - 1) && at(bottomLeftX(x, y), y - 1).isOccupiedByPlayer(player))
            return true;

        return false;
    }

    /**
     * Check if player has a chain of settlements on top right diagonal of a tile.
     *
     * @param player the player to check for.
     * @param tile   The origin tile.
     * @return True if the next three tiles on top right diagonal are occupied by player. False otherwise.
     */
    private boolean topRightDiagonalIsAChain(Player player, T tile) {
        int x = tile.x;
        int y = tile.y;

        for (int i = 0; i < 3; i++) {
            if (!isWithinBounds(x, y))
                return false;

            x = topRightX(x, y);
            y++;

            if (!at(x, y).isOccupiedByPlayer(player))
                return false;
        }
        return true;
    }

    /**
     * Check if player has a chain of settlements on top left diagonal of a tile.
     *
     * @param player the player to check for.
     * @param tile   The origin tile.
     * @return True if the next three tiles on top left diagonal are occupied by player. False otherwise.
     */
    private boolean topLeftDiagonalIsAChain(Player player, T tile) {
        int x = tile.x;
        int y = tile.y;

        for (int i = 0; i < 3; i++) {
            if (!isWithinBounds(x, y))
                return false;

            x = topLeftX(x, y);
            y++;

            if (!at(x, y).isOccupiedByPlayer(player))
                return false;
        }
        return true;
    }

    /**
     * Check if player has a chain of settlements on bottom right diagonal of a tile.
     *
     * @param player the player to check for.
     * @param tile   The origin tile.
     * @return True if the next three tiles on bottom right diagonal are occupied by player. False otherwise.
     */
    private boolean bottomRightDiagonalIsAChain(Player player, T tile) {
        int x = tile.x;
        int y = tile.y;

        for (int i = 0; i < 3; i++) {
            if (!isWithinBounds(x, y))
                return false;

            x = bottomRightX(x, y);
            y--;

            if (!at(x, y).isOccupiedByPlayer(player))
                return false;
        }
        return true;
    }

    /**
     * Check if player has a chain of settlements on bottom left diagonal of a tile.
     *
     * @param player the player to check for.
     * @param tile   The origin tile.
     * @return True if the next three tiles on bottom left diagonal are occupied by player. False otherwise.
     */
    private boolean bottomLeftDiagonalIsAChain(Player player, T tile) {
        int x = tile.x;
        int y = tile.y;

        for (int i = 0; i < 3; i++) {
            if (!isWithinBounds(x, y))
                return false;

            x = bottomLeftX(x, y);
            y--;

            if (!at(x, y).isOccupiedByPlayer(player))
                return false;
        }
        return true;
    }

    /**
     * Check if a tile is the front or back part of a chain that is occupied by a player.
     *
     * @param player the player to check for.
     * @param tile   The origin tile.
     * @return True if tile is a part of a chain. False otherwise.
     */
    public boolean freeTileIsInFrontOrBackOfAChain(Player player, T tile) {
        int x = tile.x;
        int y = tile.y;

        if(!tile.isOccupied())
            return false;

        // Check if chain is on right side
        if (isWithinBounds(x + 3, y)
                && at(x + 1, y).isOccupiedByPlayer(player)
                && at(x + 2, y).isOccupiedByPlayer(player)
                && at(x + 3, y).isOccupiedByPlayer(player))
            return true;

        // Check if chain is on left side
        if (isWithinBounds(x - 3, y)
                && at(x - 1, y).isOccupiedByPlayer(player)
                && at(x - 2, y).isOccupiedByPlayer(player)
                && at(x - 3, y).isOccupiedByPlayer(player))
            return true;

        // Check if chain is on diagonals
        if (topRightDiagonalIsAChain(player, tile)
                || topLeftDiagonalIsAChain(player, tile)
                || bottomRightDiagonalIsAChain(player, tile)
                || bottomLeftDiagonalIsAChain(player, tile))
            return true;

        return false;
    }

    /**
     * Gets all free tiles that are in front or back of a chain of settlements that is occupied by the player.
     *
     * @param player the player to check for.
     * @return All free tiles that are in front or back of a chain of settlements.
     */
    public Set<T> allFreeTilesInFrontOrBackOfAChain(Player player) {
        Set<T> freeTiles = new HashSet<>();

        for (int y = 0; y < mapWidth; y++) {
            for (int x = 0; y < mapWidth; x++) {

                if (!at(x, y).isTilePlaceable() && !playerHasASettlementInSurrounding(player, at(x, y)))
                    continue;

                else if (freeTileIsInFrontOrBackOfAChain(player, at(x, y))) {
                    freeTiles.add(at(x, y));

                }
            }
        }
        return freeTiles;
    }


    /**
     * Get all free tiles at the border of the map.
     *
     * @return All free tiles at the border of the map.
     */
    public Set<T> allFreeTilesOnBorderOfMap() {
        Set<T> freeTiles = new HashSet<>();

        for (int x = 0; x < mapWidth; x++) {

            if (at(x, 0).isTilePlaceable()) {
                // top border
                freeTiles.add(at(x, 0));
            }

            if (at(x, mapWidth - 1).isTilePlaceable()) {
                // bottom border
                freeTiles.add(at(x, mapWidth - 1));
            }
        }

        for (int y = 0; y < mapWidth; y++) {

            if (at(0, y).isTilePlaceable()) {
                // left border
                freeTiles.add(at(0, y));
            }

            if (at(mapWidth, y).isTilePlaceable()) {
                // right border
                freeTiles.add(at(mapWidth, y));
            }
        }

        return freeTiles;
    }


}
