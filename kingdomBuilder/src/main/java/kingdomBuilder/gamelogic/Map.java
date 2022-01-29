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
import java.util.stream.Collectors;

import static kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a map.
 */
public class Map implements Iterable<Tile> {

    /**
     * Represents the amount of Tokens that a special place has at the start of the game.
     */
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
    public Map(int startingTokenCount,
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
                        new Tile(x + quadrantWidth, y, bottomLeft[y * quadrantWidth + x], startingTokenCount);
            }
        }

        // combines bottom left and bottom right into tiles array
        for (int y = 0; y < quadrantWidth; y++) {
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomLeft(x, y, quadrantWidth)] =
                        new Tile(x, y + quadrantWidth, topRight[y * quadrantWidth + x], startingTokenCount);
            }
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomRight(x, y, quadrantWidth)] =
                        new Tile(x + quadrantWidth, y + quadrantWidth, bottomRight[y * quadrantWidth + x], startingTokenCount);
            }
        }
    }

    /**
     * Get the position of a 1D array from a 2D index.
     *
     * @param x     the x position of the 2D array.
     * @param y     the y position of the 2D array.
     * @param width the height/width of the 2D array.
     * @return The position in the 1D array.
     */
    protected static int to1DIndex(int x, int y, int width) {
        return y * width + x;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the top left quadrant.
     *
     * @param x     the x position of the 2D array.
     * @param y     the y position of the 2D array.
     * @param width the height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    protected static int to1DIndexTopLeft(int x, int y, int width) {
        return y * 2 * width + x;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the top right quadrant.
     *
     * @param x     the x position of the 2D array.
     * @param y     the y position of the 2D array.
     * @param width the height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    protected static int to1DIndexTopRight(int x, int y, int width) {
        return y * 2 * width + x + width;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the bottom left quadrant.
     *
     * @param x     the x position of the 2D array.
     * @param y     the y position of the 2D array.
     * @param width the height/width of the 2D quadrant.
     * @return The position in the 1D array.
     */
    protected static int to1DIndexBottomLeft(int x, int y, int width) {
        return width * 2 * width + y * 2 * width + x;
    }

    /**
     * Get the position of the 1D game map from a 2D index for the bottom right quadrant.
     *
     * @param x     the x position of the 2D array.
     * @param y     the y position of the 2D array.
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
    public Tile at(int x, int y) {
        if (!isWithinBounds(x, y))
            throw new IndexOutOfBoundsException();

        return tiles[to1DIndex(x, y, mapWidth)];
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
    public Iterator<Tile> surroundingTilesIterator(int x, int y) {

        return new Iterator<Tile>() {
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
                        if (bottomLeftX(x, y) >= 0 && y + 1 < mapWidth) {
                            state = 4;
                            return true;
                        }
                    case 5:
                        // bottom right
                        if (bottomRightX(x, y) < mapWidth && y + 1 < mapWidth) {
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
    public Iterator<Tile> iterator() {
        return Arrays.stream(tiles).iterator();
    }

    private class SurroundingTilesCollector implements Collector<Tile, Set<Tile>, Set<Tile>> {

        private final Set<Tile> originalTiles;

        private SurroundingTilesCollector(Set<Tile> tiles) {
            super();
            this.originalTiles = tiles;
        }

        @Override
        public Supplier<Set<Tile>> supplier() {
            return HashSet::new;
        }

        @Override
        public BiConsumer<Set<Tile>, Tile> accumulator() {
            return (set, tile) -> {
                for (var it = surroundingTilesIterator(tile.x, tile.y); it.hasNext(); ) {
                    var surroundingTile = it.next();
                    if (!originalTiles.contains(surroundingTile)) set.add(surroundingTile);
                }
            };
        }

        @Override
        public BinaryOperator<Set<Tile>> combiner() {
            return (set1, set2) -> {
                set1.addAll(set2);
                return set1;
            };
        }

        @Override
        public Function<Set<Tile>, Set<Tile>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
        }
    }

    // TODO: JavaDoc!
    public SurroundingTilesCollector toSurroundingTilesSet(Set<Tile> tiles) {
        return new SurroundingTilesCollector(tiles);
    }

    /**
     * Check if settlement of a player has at least one neighbour.
     *
     * @param player the player to check-
     * @param tile   tile to look for.
     * @return True if player has a neighbouring settlement. False otherwise.
     */
    public boolean playerHasASettlementInSurrounding(Player player, Tile tile) {
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
        return isWithinBounds(bottomLeftX(x, y), y - 1) && at(bottomLeftX(x, y), y - 1).isOccupiedByPlayer(player);
    }

    /**
     * Check if player has a chain of settlements on top right diagonal of a tile.
     *
     * @param player the player to check for.
     * @param tile   the origin tile.
     * @return True if the next three tiles on top right diagonal are occupied by player. False otherwise.
     */
    private boolean topRightDiagonalIsAChain(Player player, Tile tile) {
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
     * @param tile   the origin tile.
     * @return True if the next three tiles on top left diagonal are occupied by player. False otherwise.
     */
    private boolean topLeftDiagonalIsAChain(Player player, Tile tile) {
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
     * @param tile   the origin tile.
     * @return True if the next three tiles on bottom right diagonal are occupied by player. False otherwise.
     */
    private boolean bottomRightDiagonalIsAChain(Player player, Tile tile) {
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
     * @param tile   the origin tile.
     * @return True if the next three tiles on bottom left diagonal are occupied by player. False otherwise.
     */
    private boolean bottomLeftDiagonalIsAChain(Player player, Tile tile) {
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
     * @param tile   the origin tile.
     * @return True if tile is a part of a chain. False otherwise.
     */
    public boolean freeTileIsInFrontOrBackOfAChain(Player player, Tile tile) {
        int x = tile.x;
        int y = tile.y;

        if (!tile.isOccupied())
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
        return topRightDiagonalIsAChain(player, tile)
                || topLeftDiagonalIsAChain(player, tile)
                || bottomRightDiagonalIsAChain(player, tile)
                || bottomLeftDiagonalIsAChain(player, tile);
    }

    /**
     * Gets all free tiles that are in front or back of a chain of settlements that is occupied by the player.
     *
     * @param player the player to check for.
     * @return All free tiles that are in front or back of a chain of settlements.
     */
    public Set<Tile> allFreeTilesInFrontOrBackOfAChain(Player player) {
        Set<Tile> freeTiles = new HashSet<>();

        for (int y = 0; y < mapWidth; y++) {
            for (int x = 0; y < mapWidth; x++) {

                if (at(x, y).tileType != TileType.WATER
                        && at(x, y).isTilePlaceable()
                        && playerHasASettlementInSurrounding(player, at(x, y))
                        && freeTileIsInFrontOrBackOfAChain(player, at(x, y)))

                    freeTiles.add(at(x, y));
            }
        }
        return freeTiles;
    }

    /**
     * Gets all settlements of a player at the border of the map.
     *
     * @param player the player to look for.
     * @return all settlements of player at border of map.
     */
    public Set<Tile> allSettlementsOfPlayerOnBorderOfMap(Player player) {
        Set<Tile> allSettlementsOfPlayerOnBorder = new HashSet<>();

        for (int x = 0; x < mapWidth; x++) {

            if (at(x, 0).isOccupiedByPlayer(player)) {
                // top border
                allSettlementsOfPlayerOnBorder.add(at(x, 0));
            }

            if (at(x, mapWidth - 1).isOccupiedByPlayer(player)) {
                // bottom border
                allSettlementsOfPlayerOnBorder.add(at(x, mapWidth - 1));
            }
        }

        for (int y = 0; y < mapWidth; y++) {

            if (at(0, y).isOccupiedByPlayer(player)) {
                // left border
                allSettlementsOfPlayerOnBorder.add(at(0, y));
            }

            if (at(mapWidth, y).isOccupiedByPlayer(player)) {
                // right border
                allSettlementsOfPlayerOnBorder.add(at(mapWidth, y));
            }
        }

        return allSettlementsOfPlayerOnBorder;
    }


    /**
     * Gets all free tiles that are skipped from a given position.
     *
     * @param x the x coordinate of a tile to skip.
     * @param y the y coordinate of at tile to skip.
     * @return all free tiles that can be placed on that skipped position.
     */
    public Set<Tile> oneTileSkippedSurroundingTiles(int x, int y) {
        Set<Tile> freeTiles = new HashSet<>();
        int tempX, tempY;

        // top left diagonal
        tempX = topLeftX(topLeftX(x, y), y + 1);
        tempY = y + 2;

        if (isWithinBounds(tempX, tempY)
                && at(tempX, tempY).isTilePlaceable()
                && at(tempX, tempY).tileType != TileType.WATER)

            freeTiles.add(at(tempX, tempY));


        // top right diagonal
        tempX = topRightX(topRightX(x, y), y + 1);
        tempY = y + 2;

        if (isWithinBounds(tempX, tempY)
                && at(tempX, tempY).isTilePlaceable()
                && at(tempX, tempY).tileType != TileType.WATER)
            freeTiles.add(at(tempX, tempY));


        // left
        if (isWithinBounds(x - 2, y)
                && at(x - 2, y).isTilePlaceable()
                && at(tempX, tempY).tileType != TileType.WATER)

            freeTiles.add(at(x - 2, y));


        // right
        if (isWithinBounds(x + 2, y)
                && at(x - 2, y).isTilePlaceable()
                && at(tempX, tempY).tileType != TileType.WATER)

            freeTiles.add(at(x + 2, y));


        // bottom left diagonal
        tempX = bottomLeftX(bottomLeftX(x, y), y - 1);
        tempY = y - 2;

        if (isWithinBounds(tempX, tempY)
                && at(tempX, tempY).isTilePlaceable()
                && at(tempX, tempY).tileType != TileType.WATER)

            freeTiles.add(at(tempX, tempY));


        // bottom right diagonal
        tempX = bottomRightX(bottomRightX(x, y), y - 1);
        tempY = y - 2;

        if (isWithinBounds(tempX, tempY)
                && at(tempX, tempY).isTilePlaceable()
                && at(tempX, tempY).tileType != TileType.WATER)

            freeTiles.add(at(tempX, tempY));

        return freeTiles;
    }

    /**
     * Get all surrounding tiles of a given target tile.
     *
     * @param x the x coordinate of the target tile.
     * @param y the y coordinate of the target tile.
     * @return All tiles that surrounding target tile.
     */
    public Set<Tile> surroundingTiles(int x, int y) {
        if (!isWithinBounds(x, y)) {
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
        if (bottomLeftX(x, y) >= 0 && y + 1 < mapWidth)
            surroundingTiles.add(at(bottomLeftX(x, y), y + 1));

        // bottom right
        if (bottomRightX(x, y) < mapWidth && y + 1 < mapWidth)
            surroundingTiles.add(at(bottomRightX(x, y), y + 1));

        return surroundingTiles;
    }

    /**
     * Check if next to the tile is a token and return it.
     *
     * @param x the x coordinate of the tile.
     * @param y the y coordinate of the tile.
     * @return The Token that is next to the settlement or null if there is no token;
     */
    public TileType specialPlaceInSurrounding(int x, int y) {
        for (Tile tile : surroundingTiles(x, y)) {
            if (tokenType.contains(tile.tileType)) {
                return tile.tileType;
            }
        }
        return null;
    }

    /**
     * Checks whether there is another settlement of the player on surrounding tiles.
     *
     * @param player the player that to check.
     * @param x      the x coordinate of the Tile.
     * @param y      the x coordinate of the Tile.
     * @return True if the player has another settlement on surrounding tile. False otherwise.
     */
    public boolean settlementOfPlayerOnSurroundingTiles(Player player, int x, int y) {
        if (!isWithinBounds(x, y))
            return false;

        for (Tile tile : surroundingTiles(x, y)) {
            if (tile.isOccupiedByPlayer(player))
                return true;
        }
        return false;
    }

    /**
     * Checks whether there is another settlement of the player on surrounding tiles AND the terrain is the same.
     *
     * @param player the player that to check.
     * @param x      the x coordinate of the Tile.
     * @param y      the x coordinate of the Tile.
     * @return True if the player has another settlement on surrounding tile within same terrain. False otherwise.
     */
    public boolean settlementOfPlayerOnSurroundingTilesOnTerrain(Player player, TileType terrain, int x, int y) {
        if (!isWithinBounds(x, y) && nonPlaceableTileTypes.contains(terrain))
            return false;

        for (Tile tile : surroundingTiles(x, y)) {
            if (tile.isOccupiedByPlayer(player) && tile.tileType == terrain)
                return true;
        }
        return false;
    }

    /**
     * Gets the entire terrain of a type.
     *
     * @param terrain the terrain to filter.
     * @return All tiles of a type.
     */
    public Set<Tile> getEntireTerrain(TileType terrain) {
        if (nonPlaceableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Tile> entireTerrain = new HashSet<>();

        // return Arrays.stream(tiles).filter(t -> t.isTilePlaceable()).collect(Collectors.toSet());
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
     * @param terrain the terrain to get.
     * @return all free tiles of the terrain.
     */
    public Set<Tile> freeTilesOnTerrain(TileType terrain) {
        if (nonPlaceableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a landscape!");

        Set<Tile> freeTiles = new HashSet<>();

        for (Tile tile : getEntireTerrain(terrain)) {

            if (tile.isTilePlaceable())
                freeTiles.add(tile);
        }

        return freeTiles;
    }

    /**
     * Checks if there is only one settlement of a player next to a special place.
     *
     * @param player the player to check.
     * @param x      the x position of the special place.
     * @param y      the y position of the special place.
     * @return true if player has only one settlement next to a speacial place.
     */
    public boolean playerHasOnlyOneSettlementNextToSpecialPlace(Player player, int x, int y) {
        Tile token;
        int counter = 0;

        for (Tile tile : surroundingTiles(x, y)) {
            token = tile;
            if (at(token.x, token.y).occupiedBy() == player)
                counter++;
        }

        return counter == 1;
    }

    /**
     * Get all positions where the player has a settlement.
     *
     * @param player the player as the owner of the settlements.
     * @return all settlements of the player.
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

    /**
     * Checks if terrain is filled with settlement and therefore has no placeable tiles.
     *
     * @param terrain the terrain to check for.
     * @return true if terrain has no more free tiles. False otherwise.
     */
    public boolean terrainIsFull(TileType terrain){

        for(Tile tile : getEntireTerrain(terrain)){
            if(tile.isTilePlaceable()){
                return false;
            }
        }

        return true;
    }
}