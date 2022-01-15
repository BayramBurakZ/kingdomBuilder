package kingdomBuilder.gamelogic;

import kingdomBuilder.gamelogic.Game.*;

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
     * Check if tile has any tokens left.
     *
     * @return True if there are tokens left. False otherwise.
     */
    public boolean hasTokens() {
        return (remainingTokens > 0);
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
     * @param ownerOfSettlement Player that places a settlement.
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
            throw new NullPointerException("Tile is not occupied!");

        Player previousPlayer = occupiedBy;
        occupiedBy = null;

        return previousPlayer;
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
