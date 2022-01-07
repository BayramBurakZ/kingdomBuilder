package kingdomBuilder.gamelogic;

import kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a tile.
 */
public class Tile {

    private TileType tileType;
    private Player occupiedBy;
    private int remainingTokens;

    public Tile(TileType tileType, int remainingTokens) {

        this.tileType = tileType;
        this.occupiedBy = null;
        this.remainingTokens = remainingTokens;
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

    public boolean isOccupiedByPlayer(Player player) {

        if (occupiedBy == player)
            return true;

        return false;
    }

    public boolean isOccupied() {

        if (occupiedBy != null)
            return true;

        return false;
    }


    /**
     * Gets the tile type of the tile.
     *
     * @return The tile type of the tile.
     */
    public TileType getTileType() {
        return tileType;
    }

}
