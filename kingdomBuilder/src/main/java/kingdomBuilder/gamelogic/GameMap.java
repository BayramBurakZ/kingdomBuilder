package kingdomBuilder.gamelogic;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains the data of a map.
 */
public class GameMap implements Iterable<Tile> {

    /**
     * Represents the amount of Tokens that a special place has at the start of the game.
     */
    public static final int DEFAULT_STARTING_TOKEN_COUNT = 2;

    /**
     * Represents the tiles of the map in a 1D array.
     */
    private final Tile[] tiles;

    private final Set<Tile> tileSet;

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
    public GameMap(int startingTokenCount,
                   TileType[] topLeft,
                   TileType[] topRight,
                   TileType[] bottomLeft,
                   TileType[] bottomRight)
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
                        new Tile(x, y, topLeft[x * quadrantWidth + y], startingTokenCount);
            }
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexTopRight(x, y, quadrantWidth)] =
                        new Tile(x + quadrantWidth, y, topRight[x * quadrantWidth + y], startingTokenCount);
            }
        }

        // combines bottom left and bottom right into tiles array
        for (int y = 0; y < quadrantWidth; y++) {
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomLeft(x, y, quadrantWidth)] =
                        new Tile(x, y + quadrantWidth, bottomLeft[x * quadrantWidth + y], startingTokenCount);
            }
            for (int x = 0; x < quadrantWidth; x++) {
                tiles[to1DIndexBottomRight(x, y, quadrantWidth)] =
                        new Tile(x + quadrantWidth, y + quadrantWidth, bottomRight[x * quadrantWidth + y], startingTokenCount);
            }
        }

        tileSet = Set.of(tiles);
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
     * Calculates the x position for the tile that lies top left at the specified distance from the tile with the
     * given coordinates.
     *
     * @param x        the x coordinate of the original tile.
     * @param y        the y coordinate of the original tile.
     * @param distance the distance from the original tile.
     * @return The x coordinate of the tile that lies top left from the original tile.
     */
    protected static int topLeftX(int x, int y, int distance) {
        // TODO: remove the loop and calculate this properly
        for (int i = 0; i < distance; i++)
            x = topLeftX(x, y - i);
        return x;
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
     * Calculates the x position for the tile that lies top right at the specified distance from the tile with the
     * given coordinates.
     *
     * @param x        the x coordinate of the original tile.
     * @param y        the y coordinate of the original tile.
     * @param distance the distance from the original tile.
     * @return The x coordinate of the tile that lies top right from the original tile.
     */
    protected static int topRightX(int x, int y, int distance) {
        // TODO: remove the loop and calculate this properly
        for (int i = 0; i < distance; i++)
            x = topRightX(x, y - i);
        return x;
    }

    /**
     * Calculates the x position for the tile that lies bottom left from the tile with the given coordinates.
     *
     * @param x the x coordinate of the original tile.
     * @param y the y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom left from the original tile.
     */
    protected static int bottomLeftX(int x, int y) {
        return topLeftX(x, y);
    }

    /**
     * Calculates the x position for the tile that lies bottom left at the specified distance from the tile with the
     * given coordinates.
     *
     * @param x        the x coordinate of the original tile.
     * @param y        the y coordinate of the original tile.
     * @param distance the distance from the original tile.
     * @return The x coordinate of the tile that lies bottom left from the original tile.
     */
    protected static int bottomLeftX(int x, int y, int distance) {
        // TODO: remove the loop and calculate this properly
        for (int i = 0; i < distance; i++)
            x = bottomLeftX(x, y + i);
        return x;
    }

    /**
     * Calculates the x position for the tile that lies bottom right from the tile with the given coordinates.
     *
     * @param x the x coordinate of the original tile.
     * @param y the y coordinate of the original tile.
     * @return The x coordinate of the tile that lies bottom right from the original tile.
     */
    protected static int bottomRightX(int x, int y) {
        return topRightX(x, y);
    }

    /**
     * Calculates the x position for the tile that lies bottom right at the specified distance from the tile with the
     * given coordinates.
     *
     * @param x        the x coordinate of the original tile.
     * @param y        the y coordinate of the original tile.
     * @param distance the distance from the original tile.
     * @return The x coordinate of the tile that lies bottom right from the original tile.
     */
    protected static int bottomRightX(int x, int y, int distance) {
        // TODO: remove the loop and calculate this properly
        for (int i = 0; i < distance; i++)
            x = bottomRightX(x, y + i);
        return x;
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
        return (x >= 0 && y >= 0 && x < mapWidth && y < mapWidth);
    }

    /**
     * Returns an iterator that contains all surrounding tiles from a given hexagon.
     *
     * @param x the x coordinate of the Tile.
     * @param y the y coordinate of the Tile.
     * @return All surrounding Tiles.
     */
    /*public Iterator<Tile> surroundingTilesIterator(int x, int y) {

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
                        if (x + 1 < mapWidth) {
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
    }*/

    /**
     * Returns a stream of all the tiles of the map.
     *
     * @return the stream of all the tiles of the map.
     */
    public Stream<Tile> stream() {
        return Arrays.stream(tiles);
    }

    /**
     * Returns an iterator over all the tiles of the map.
     *
     * @return the iterator over all the tiles of the map.
     */
    @NotNull
    @Override
    public Iterator<Tile> iterator() {
        return Arrays.stream(tiles).iterator();
    }

    /*
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
    */
    ///**
    // * Returns a set of all surrounding tiles of the given tiles.
    // * @param tiles tiles of the map.
    // * @return a set of all surrounding tiles of the given tiles.
    // */
    //public SurroundingTilesCollector toSurroundingTilesSet(Set<Tile> tiles) {
    //    return new SurroundingTilesCollector(tiles);
    //}

    /**
     * Gets all the tiles of the map.
     *
     * @return all tiles of the map.
     */
    public Set<Tile> getTiles() {
        return tileSet;
    }

    /**
     * Gets all the tiles of the map of the specified placeable terrain type.
     *
     * @param terrain the placeable terrain type to filter by.
     * @return all tiles of the given placeable terrain type.
     */
    public Set<Tile> getTiles(TileType terrain) {
        if (TileType.nonPlaceableTileTypes.contains(terrain))
            throw new InvalidParameterException("The specified terrain is not a placeable terrain type!");

        return stream().filter(t -> t.tileType == terrain).collect(Collectors.toSet());
    }

    /**
     * Gets all Tiles at border of map.
     *
     * @return all tiles that are at the border of the map.
     */
    public Set<Tile> getTilesAtBorder() {
        Set<Tile> border = new HashSet<>();

        for (int x = 0; x < mapWidth; x++) {
            border.add(at(x, 0));
            border.add(at(x, mapWidth - 1));
        }

        for (int y = 0; y < mapWidth; y++) {
            border.add(at(0, y));
            border.add(at(mapWidth - 1, y));
        }

        return border;
    }

    /**
     * Gets all free tiles that are next to a player's settlement.
     *
     * @param player the player of the settlements.
     * @return
     */
    public Set<Tile> getPlaceableTilesAtBorder(Player player) {
        //Set<Tile> allPossiblePlacementsAtBorder = new HashSet<>();

        Set<Tile> tilesOnBorder = getTilesAtBorder().stream().filter(tile -> !tile.isBlocked()
                        && tile.hasSurroundingSettlement(this, player )).collect(Collectors.toSet());

        return tilesOnBorder.isEmpty() ?
                getTilesAtBorder().stream().filter(t -> !t.isBlocked()).collect(Collectors.toSet()) : tilesOnBorder;

        // TODO:
        /*
        for (Tile tile : map.allSettlementsOfPlayerOnBorderOfMap(player)) {
            if (tile.x == 0 || tile.x == map.mapWidth - 1) {

                if (map.isWithinBounds(tile.x, tile.y - 1)
                        && map.at(tile.x, tile.y - 1).isTilePlaceable()
                        && map.at(tile.x, tile.y - 1).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x, tile.y - 1));

                if (map.isWithinBounds(tile.x, tile.y + 1)
                        && map.at(tile.x, tile.y + 1).isTilePlaceable()
                        && map.at(tile.x, tile.y + 1).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x, tile.y + 1));
            }

            if (tile.y == 0 || tile.y == map.mapWidth - 1) {

                if (map.isWithinBounds(tile.x - 1, tile.y)
                        && map.at(tile.x + 1, tile.y).isTilePlaceable()
                        && map.at(tile.x - 1, tile.y).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x - 1, tile.y));

                if (map.isWithinBounds(tile.x + 1, tile.y)
                        && map.at(tile.x + 1, tile.y).isTilePlaceable()
                        && map.at(tile.x + 1, tile.y).tileType != TileType.WATER)

                    allPossiblePlacementsAtBorder.add(map.at(tile.x + 1, tile.y));
            }
        }
        */

      //  return allPossiblePlacementsAtBorder;
    }

    /**
     * Gets all possible positions to place a settlement for a player on a given terrain next to
     * other settlements.
     *
     * @param player  the player to check for.
     * @param terrain the terrain to check for.
     * @return All tiles that can be placed next to other settlements.
     */
    protected Set<Tile> getAllPlaceableTilesNextToSettlements(Player player, TileType terrain) {
        if (!TileType.placeableTileTypes.contains(terrain) && terrain != null)
            throw new InvalidParameterException("not a landscape!");

        return getTiles(terrain).stream().filter(tile ->
                !tile.isBlocked()
                && tile.hasSurroundingSettlement(this, player)).collect(Collectors.toSet());
    }

    /**
     * Gives a preview of all possible tiles to place a settlement.
     *
     * @param player  the player to check for.
     * @param terrain the terrain the player has.
     * @return A set of all positions a player can place a settlement.
     */
    protected Set<Tile> getAllPlaceableTiles(Player player, TileType terrain) {
        Set<Tile> allPossiblePlacements = getAllPlaceableTilesNextToSettlements(player, terrain);
        return (allPossiblePlacements.isEmpty()) ?
                getTiles(terrain).stream().filter(t -> !t.isBlocked()).collect(Collectors.toSet())
                : allPossiblePlacements;
    }

    /**
     * Get all positions where the player has a settlement.
     *
     * @param player the player as the owner of the settlements.
     * @return all settlements of the player.
     */
    public Set<Tile> getSettlements(Player player) {

        return stream().filter(t -> t.occupiedBy == player).collect(Collectors.toSet());
    }

    /**
     * Adds all settlements of a group of settlements into the given Set of Tiles.
     *
     * @param tiles the result.
     * @param player the player of the group.
     * @param x the x-coordinate of the beginning settlement.
     * @param y the y-coordinate of the beginning settlement.
     */
    public void getSettlementGroup(Set<Tile> tiles, Player player, int x, int y) {
        Set<Tile> surroundings = at(x,y).surroundingSettlements(this, player);

        tiles.add(at(x,y));

        // remove all checked tiles
        surroundings.removeAll(tiles);

        if (surroundings.isEmpty())
            return;

        // check surrounding of new tiles
        for (Tile t : surroundings) {
            getSettlementGroup(tiles, player, t.x, t.y);
        }

        return;
    }
}