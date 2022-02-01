package kingdomBuilder.gamelogic;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a tile.
 */
public class Tile {

    /**
     * The x and y coordinate of the tile.
     */
    public final int x, y;

    /**
     * The tile type of the tile.
     */
    public final Game.TileType tileType;

    /**
     * The player whose settlement is occupying the tile.
     */
    protected Player occupiedBy;

    /**
     * The amount of tokens that players can still receive from this tile.
     */
    protected int remainingTokens;

    /**
     * Constructs a new TileReadOnly object with the given coordinate, the TileType and the amount of tokens.
     *
     * @param x               the x-coordinate.
     * @param y               the y-coordinate.
     * @param tileType        the tileType.
     * @param remainingTokens the amount of tokens that remains on that tile.
     */
    public Tile(int x, int y, Game.TileType tileType, int remainingTokens) {

        this.x = x;
        this.y = y;
        this.tileType = tileType;

        this.occupiedBy = null;
        this.remainingTokens = remainingTokens;
    }

    /**
     * Checks whether the tile is occupied by any player or obstructed due to terrain.
     *
     * @return True if it is occupied. False otherwise.
     */
    public boolean isBlocked() {
        return !placeableTileTypes.contains(tileType) || (occupiedBy != null);
    }

    /**
     * Gets the player that is occupying the tile.
     *
     * @return The player that is occupying the tile.
     */
    public Player occupiedBy() {
        return occupiedBy;
    }

    /**
     * Check whether the tile at given index is at the border of the map.
     *
     * @param map the map containing the tile.
     * @return Whether the tile is at the border.
     */
    public boolean isAtBorder(Map map) {
        return (x == 0 || y == 0 || x == map.mapWidth - 1 || y == map.mapWidth - 1);
    }

    /**
     * Checks if the tile has a token left.
     *
     * @return True if it has tokens left. False otherwise.
     */
    public boolean hasTokens() {
        if (!tokenType.contains(tileType))
            throw new RuntimeException("The tile is not a special place!");

        return (remainingTokens > 0);
    }

    /**
     * Checks if two tiles are neighbours in the hexagon map.
     *
     * @param other the second tile.
     * @return Whether both tiles are neighbours.
     */
    public boolean areNeighbouringTiles(Tile other) {
        // Compare Y
        if (Math.abs(y - other.y) > 1)
            return false;
        // Neighbour in same row
        if (y == other.y && Math.abs(x - other.x) > 1)
            return false;
        if (y % 2 == 0) {
            // even row
            // top/bottom left or top/bottom right
            return x == other.x - 1 || x == other.x;
        } else {
            // odd row
            // top/bottom right or top/bottom left
            return x == other.x + 1 || x == other.x;
        }
    }

    /**
     * Check if a tile is at the front or back part of a chain of settlements that are owned by the specified player.
     *
     * @param map    the map containing the tile.
     * @param player the player to check for.
     * @return True if tile is a part of a chain. False otherwise.
     */
    public boolean isAtEndOfAChain(Map map, Player player) {

        // TODO: iterator/stream

        if (isBlocked())
            return false;

        // Check if chain is on right side
        if (map.isWithinBounds(x + 3, y)
                && (map.at(x + 1, y).occupiedBy() == player)
                && (map.at(x + 2, y).occupiedBy() == player)
                && (map.at(x + 3, y).occupiedBy() == player))
            return true;

        // Check if chain is on left side
        if (map.isWithinBounds(x - 3, y)
                && (map.at(x - 1, y).occupiedBy() == player)
                && (map.at(x - 2, y).occupiedBy() == player)
                && (map.at(x - 3, y).occupiedBy() == player))
            return true;

        // top right
        if (map.isWithinBounds(Map.topRightX(x, y, 3), y - 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (map.at(Map.topRightX(x, y, i), y - i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        // top left
        if (map.isWithinBounds(Map.topLeftX(x, y, 3), y - 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (map.at(Map.topLeftX(x, y, i), y - i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        // bottom right
        if (map.isWithinBounds(Map.bottomRightX(x, y, 3), y + 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (map.at(Map.bottomRightX(x, y, i), y + i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        // bottom left
        if (map.isWithinBounds(Map.bottomLeftX(x, y, 3), y + 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (map.at(Map.bottomLeftX(x, y, i), y + i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        return false;
    }

    /**
     * Get all the surrounding tiles of the specified tile.
     *
     * @return all surrounding tiles of the specified tile.
     */
    public Set<Tile> surroundingTiles(Map map) {
        if (!map.isWithinBounds(x, y)) {
            return null;
        }

        Set<Tile> surroundingTiles = new HashSet<>();

        // top left
        if (map.topLeftX(x, y) >= 0 && y > 0)
            surroundingTiles.add(map.at(Map.topLeftX(x, y), y - 1));

        // top right
        if (Map.topRightX(x, y) < map.mapWidth && y > 0)
            surroundingTiles.add(map.at(Map.topRightX(x, y), y - 1));

        // left
        if (x - 1 >= 0)
            surroundingTiles.add(map.at(x - 1, y));

        // right
        if (x + 1 < map.mapWidth)
            surroundingTiles.add(map.at(x + 1, y));

        // bottom left
        if (Map.bottomLeftX(x, y) >= 0 && y + 1 < map.mapWidth)
            surroundingTiles.add(map.at(Map.bottomLeftX(x, y), y + 1));

        // bottom right
        if (Map.bottomRightX(x, y) < map.mapWidth && y + 1 < map.mapWidth)
            surroundingTiles.add(map.at(Map.bottomRightX(x, y), y + 1));

        // TODO: eventually return streams everywhere
        return surroundingTiles;
    }

    /**
     * Gets all free tiles that are skipped from a given position.
     *
     * @param map the map containing the tile.
     * @return all free tiles that can be placed on that skipped position.
     */
    public Set<Tile> surroundingTilesPaddock(Map map) {
        Set<Tile> freeTiles = new HashSet<>();
        int tempX, tempY;

        // TODO: iterator
        // top left diagonal
        tempX = Map.topLeftX(x, y, 2);
        tempY = y - 2;

        if (map.isWithinBounds(tempX, tempY) && !map.at(tempX, tempY).isBlocked())
            freeTiles.add(map.at(tempX, tempY));

        // top right diagonal
        tempX = Map.topRightX(x, y, 2);
        tempY = y - 2;

        if (map.isWithinBounds(tempX, tempY) && !map.at(tempX, tempY).isBlocked())
            freeTiles.add(map.at(tempX, tempY));

        // left
        if (map.isWithinBounds(x - 2, y) && !map.at(x - 2, y).isBlocked())
            freeTiles.add(map.at(x - 2, y));


        // right
        if (map.isWithinBounds(x + 2, y) && !map.at(x + 2, y).isBlocked())
            freeTiles.add(map.at(x + 2, y));


        // bottom left diagonal
        tempX = Map.bottomLeftX(x, y, 2);
        tempY = y + 2;

        if (map.isWithinBounds(tempX, tempY) && !map.at(tempX, tempY).isBlocked())
            freeTiles.add(map.at(tempX, tempY));

        // bottom right diagonal
        tempX = Map.bottomRightX(x, y, 2);
        tempY = y + 2;

        if (map.isWithinBounds(tempX, tempY) && !map.at(tempX, tempY).isBlocked())
            freeTiles.add(map.at(tempX, tempY));

        return freeTiles;
    }

    /**
     * Returns the surrounding settlements owned by the specified player.
     *
     * @param map    the map containing the tile.
     * @param player the player whose settlements to look for.
     * @return all tiles with a settlement of the specified player.
     */
    public Set<Tile> surroundingSettlements(Map map, Player player) {
        /*
        HashSet<Tile> tilesWithSettlements = new HashSet<>();
        for (Tile tile : surroundingTiles(map)) {
            if (tile.occupiedBy() == player)
                tilesWithSettlements.add(tile);
        }
        return tilesWithSettlements;
        */
        return surroundingTiles(map).stream().filter(tile -> tile.occupiedBy == player)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the surrounding special places.
     *
     * @param map the map containing the tile.
     * @return all surrounding tiles that are special places.
     */
    public Set<Tile> surroundingSpecialPlaces(Map map) {
        //TODO: check for each one if the player has only one settlement on it
        /*
        Set<Tile> surroundingSpecialPlaces = new HashSet<>();
        for (Tile tile : surroundingTiles(map)) {
            if (tokenType.contains(tile.tileType)) {
                surroundingSpecialPlaces.add(tile);
            }
        }
        return surroundingSpecialPlaces;
        */
        return surroundingTiles(map).stream().filter(tile -> tokenType.contains(tile.tileType))
                .collect(Collectors.toSet());
    }

    /**
     * Check if settlement of a player has at least one neighbour.
     *
     * @param map    the map containing the tile.
     * @param player the player to check.
     * @return True if player has a neighbouring settlement. False otherwise.
     */
    public boolean hasSurroundingSettlement(Map map, Player player) {

        // TODO: iterator/stream
        //for (var tile : surroundingTiles())

        // right tile
        if (map.isWithinBounds(x + 1, y) && (map.at(x + 1, y).occupiedBy() == player))
            return true;

        // left tile
        if (map.isWithinBounds(x - 1, y) && (map.at(x - 1, y).occupiedBy() == player))
            return true;

        // top right
        if (map.isWithinBounds(Map.topRightX(x, y), y + 1)
                && (map.at(Map.topRightX(x, y), y + 1).occupiedBy() == player))
            return true;

        // top left
        if (map.isWithinBounds(Map.topLeftX(x, y), y + 1)
                && (map.at(Map.topLeftX(x, y), y + 1).occupiedBy() == player))
            return true;

        // bottom right
        if (map.isWithinBounds(Map.bottomRightX(x, y), y - 1)
                && (map.at(Map.bottomRightX(x, y), y - 1).occupiedBy() == player))
            return true;

        // bottom left
        return map.isWithinBounds(Map.bottomLeftX(x, y), y - 1)
                && (map.at(Map.bottomLeftX(x, y), y - 1).occupiedBy() == player);
    }

    ////////////////////////////////////////////////
    // Setter Methods //////////////////////////////

    /**
     * Place a settlement on the tile that belongs to the given player.
     *
     * @param ownerOfSettlement player that places a settlement.
     */
    public void placeSettlement(Player ownerOfSettlement) {
        if (occupiedBy != null)
            throw new TileIsAlreadyOccupiedException("Is already occupied by Player: " + occupiedBy.name);

        occupiedBy = ownerOfSettlement;
    }

    /**
     * Remove a settlement from the tile.
     *
     * @return The previous player that occupied the tile.
     */
    public Player removeSettlement() {
        if (occupiedBy == null)
            throw new RuntimeException("Tile is not occupied at: " + x + "/" + y);

        Player previousPlayer = occupiedBy;
        occupiedBy = null;

        return previousPlayer;
    }

    /**
     * Moves a settlement from this tile to another.
     *
     * @param destination the tile that the settlement gets moved to.
     */
    public void moveSettlement(Tile destination) {
        Player player = removeSettlement();
        destination.placeSettlement(player);
    }

    /**
     * Take a token from a special place.
     *
     * @return The token type of the special place.
     * @throws HasNoTokenException when tile is not a special place.
     */
    public TileType takeTokenFromSpecialPlace() throws HasNoTokenException {
        if (!Game.tokenType.contains(tileType))
            throw new HasNoTokenException("Can't take a token from a non special place!");
        if (remainingTokens <= 0)
            throw new HasNoTokenException("No more tokens remaining!");

        remainingTokens--;
        return tileType;
    }
}
