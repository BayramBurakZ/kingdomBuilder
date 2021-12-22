package kingdomBuilder.gamelogic;

import kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a tile.
 */
public class Tile {
    // TODO: Possibly refactor enums of tiles and tokens here and maybe merge them together into a single enum class.
    // TODO: how does a Player own a specific token?
    // TODO: Split this class into three subclasses with token, normal tile and castle OR two seperate classes.

    TileType tileType;
    TokenType tokenType;
    private Player occupiedBy;
    private int remainingTokens;

    /**
     * Place a settlement on the tile that belongs to the given player.
     *
     * @param ownerOfSettlement Player that places a settlement.
     */
    public void placeSettlement(Player ownerOfSettlement) {
        // TODO: throw exception if already occupied.
        occupiedBy = ownerOfSettlement;
    }

    /**
     * Remove a settlement from the tile.
     *
     * @return The previous player that occupied the tile.
     */
    public Player removeSettlement() {
        // TODO: throw exception if not occupied.
        Player previousPlayer = occupiedBy;
        occupiedBy = null;

        return previousPlayer;
    }

    /**
     * Take a token from a special place.
     *
     * @return The token type of the special place.
     */
    public TokenType takeTokenFromSpecialPlace() {
        // TODO: throw exception if no token is available or if it's not a special place.
        remainingTokens--;
        return tokenType;
    }
}
