package kingdomBuilder.gamelogic;

import kingdomBuilder.gamelogic.Game.*;

import java.security.InvalidParameterException;

import static kingdomBuilder.gamelogic.Game.nonPlaceableTileTypes;
import static kingdomBuilder.gamelogic.Game.tokenType;

/**
 * Contains the data of a tile.
 */
class TileReadOnly {

    /**
     * The x and y coordinate of the tile.
     */
    public final int x, y;

    /**
     * The tile type of the tile.
     */
    public final TileType tileType;

    /**
     * The player whose settlement is occupying the tile.
     */
    protected Player occupiedBy;

    /**
     * The amount of tokens that players can still receive from this tile.
     */
    protected int remainingTokens;

    public TileReadOnly(int x, int y, TileType tileType, int remainingTokens) {

        this.x = x;
        this.y = y;
        this.tileType = tileType;

        this.occupiedBy = null;
        this.remainingTokens = remainingTokens;
    }

    // TODO: JavaDoc, maybe scrap and just do getOccupyingPlayer() == player separately
    public boolean isOccupiedByPlayer(Player player) {
        return (occupiedBy == player);
    }

    /**
     * Checks whether the tile is occupied by any player.
     *
     * @return True if it is occupied. False otherwise.
     */
    public boolean isOccupied() {
        return (occupiedBy != null);
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
    public boolean isAtBorder(MapReadOnly map) {
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
     * Checks whether the tile type is placeable excluding water which is only placeable with a token.
     *
     * @return Whether the tile is placeable.
     */
    public boolean isTilePlaceable() {

        if (tokenType.contains(tileType))
            return false;
        else if (isOccupied())
            return false;
        else return !nonPlaceableTileTypes.contains(tileType);
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
}

/**
 * Contains the data of a tile.
 */
public class Tile extends TileReadOnly {

    public Tile(int x, int y, TileType tileType, int remainingTokens) {
        super(x, y, tileType, remainingTokens);
    }

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
            throw new RuntimeException("Tile is not occupied!");

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
        if (Game.tokenType.contains(tileType))
            throw new HasNoTokenException("Can't take a token from a non special place!");
        if (remainingTokens <= 0)
            throw new HasNoTokenException("No more tokens remaining!");

        remainingTokens--;
        return tileType;
    }
}
