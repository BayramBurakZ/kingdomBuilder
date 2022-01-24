package kingdomBuilder.gamelogic;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a map.
 */
class Map extends MapReadOnly<Tile> {

    /**
     * Creates the map from the given quadrants.
     *
     * @param startingTokenCount the amount of tokens that a special place contains at game start.
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
     * @param x      The x coordinate of the Tile.
     * @param y      The x coordinate of the Tile.
     * @return True if the player has another settlement on surrounding tile. False otherwise.
     */
    public boolean settlementOfPlayerOnSurroundingTiles(Player player, int x, int y) {
        for (Tile tile : surroundingTiles(x, y)) {
            if (tile.isOccupiedByPlayer(player))
                return true;
        }
        return false;
    }

    /**
     * Check if there is at least one settlement of a Player on a terrain.
     *
     * @param player  The player to check.
     * @param terrain the terrain to check.
     * @return True if there is at least one settlement on terrain.
     */
    public boolean settlementOfPlayerOnTerrain(Player player, TileType terrain) {
        if (!placeableTileTypes.contains(terrain))
            throw new InvalidParameterException("not a terrain!");

        for (Tile tile : getEntireTerrain(terrain)) {
            if (tile.occupiedBy == player)
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

            if (at(current.x, current.y).isTilePlaceable())
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

            if (at(i, 0).isTilePlaceable())
                freeTiles.add(at(i, 0));

            if (at(0, i).isTilePlaceable())
                freeTiles.add(at(0, i));

            if (at(i, mapWidth - 1).isTilePlaceable())
                freeTiles.add(at(i, mapWidth - 1));

            if (at(mapWidth - 1, i).isTilePlaceable())
                freeTiles.add(at(mapWidth - 1, i));
        }
        return freeTiles;
    }

    /**
     * Checks if there is only one settlement of a player next to a special place.
     *
     * @param player the player to check-
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