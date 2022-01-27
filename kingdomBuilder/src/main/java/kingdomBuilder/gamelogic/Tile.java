package kingdomBuilder.gamelogic;

import kingdomBuilder.gamelogic.Game.*;

import java.security.InvalidParameterException;

import static kingdomBuilder.gamelogic.Game.nonPlaceableTileTypes;
import static kingdomBuilder.gamelogic.Game.tokenType;

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
