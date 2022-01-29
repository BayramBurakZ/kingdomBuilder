package kingdomBuilder.gamelogic;

import java.util.HashMap;
import java.util.HashSet;

import static kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a player.
 */
public class Player {

    /**
     * Represents the Client ID of this player.
     */
    public final int ID;

    /**
     * Represents the name of this player.
     */
    public final String name;

    /**
     * Represents the color of this player.
     */
    public final PlayerColor color;

    /**
     * Represents the total remaining settlements of this player for the rest of the game.
     */
    int remainingSettlements;

    /**
     * Represents the tokens that this player has.
     */
    private final HashMap<TileType, Token> tokens;

    // data of the player's current/next turn
     /**
     * Represents the terrain card for this player.
     */
     private TileType terrainCard;

    /**
     * Represents the settlements that the player needs to place in this turn.
     */
    int remainingSettlementsOfTurn;

    /**
     * Class that represents a token.
     */
    private static class Token {
        HashSet<Tile> originTiles = new HashSet<>();
        int remaining;

        int total() {
            return originTiles.size();
        }
    }

    /**
     * Constructs a new player object with the given information.
     * @param ID the client id.
     * @param name the name of the client.
     * @param color the color of the client.
     * @param totalSettlements the amount of settlement that the player has.
     */
    public Player(int ID, String name, PlayerColor color, int totalSettlements) {
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.remainingSettlements = totalSettlements;
        this.tokens = new HashMap<>(Game.tokenType.size());
        tokenType.forEach(tileType -> tokens.put(tileType, new Token()));
    }

    /**
     * Updates the information for the player at the start of a turn.
     * @param terrainCard the TileType of the terrain card of the turn.
     */
    void startTurn(TileType terrainCard) {
        this.terrainCard = terrainCard;
        remainingSettlementsOfTurn = Math.min(remainingSettlements, Game.SETTLEMENTS_PER_TURN);
        tokens.forEach((tileType, token) -> token.remaining = token.total());
    }

    void endTurn() {
        // placeholder, in case it's needed later
    }

    /**
     * Gives the player a token.
     *
     * @param originTile the tile whose token is given to the player.
     */
    void addToken(Tile originTile) {
        if (!tokenType.contains(originTile.tileType))
            throw new RuntimeException("The tile's type is not a token type!");

        Token token = tokens.get(originTile.tileType);

        //if (token.originTiles.contains(originTile))
        //    throw new RuntimeException("The player already owns a token from this tile!");

        token.originTiles.add(originTile);
    }

    public boolean hasToken(Tile originTile) {
        if (!tokenType.contains(originTile.tileType))
            throw new RuntimeException("The tile's type is not a token type!");

        Token token = tokens.get(originTile.tileType);
        return token.originTiles.contains(originTile);
    }

    /**
     * Removes a token from the player's total amount of tokens of that type.
     *
     * @param originTile the tile whose token is taken from the player and returned to the tile.
     */
    void removeToken(Tile originTile) {
        if (!tokenType.contains(originTile.tileType))
            throw new RuntimeException("The tile's type is not a token type!");

        Token token = tokens.get(originTile.tileType);

        //if (!token.originTiles.contains(originTile))
        //    throw new RuntimeException("The player doesn't own a token from this tile!");

        token.originTiles.remove(originTile);
    }

    /**
     * Uses the given token by reducing the amount of remaining uses for the current turn.
     * @param tokenType the type of the token to use it.
     */
    void useToken(TileType tokenType) {
        Token token = tokens.get(tokenType);
        if (token.remaining <= 0)
            throw new RuntimeException("Tried to use a token but the player had none remaining!");
        token.remaining--;
    }

    /**
     * Check how many remaining Settlements a player has.
     *
     * @return The amount of Settlements the player has.
     */
    public boolean hasRemainingSettlements() {
        return remainingSettlements > 0;
    }

    /**
     * Represents the remaining settlements of this player.
     */
    public int getRemainingSettlements() {
        return remainingSettlements;
    }

    /**
     * Represents the terrain card for this player.
     */
    public TileType getTerrainCard() {
        return terrainCard;
    }

    /**
     * Returns the amount of tokens from a type the player has.
     *
     * @param tokenType the token type to check.
     * @return The amount of that type.
     */
    public int getRemainingTokens(TileType tokenType) {
        return tokens.get(tokenType).remaining;
    }


    /**
     * Returns the amount of tokens from a type the player has.
     *
     * @param tokenType the token type to check.
     * @return The amount of that type.
     */
    public int getTotalTokens(TileType tokenType) {
        return tokens.get(tokenType).total();
    }

    /**
     * Check if player owns a specific type of token.
     *
     * @param tokenType the token type to check.
     * @return true if he owns at least one of that token.
     */
    public boolean playerHasTokenLeft(TileType tokenType) {
        if (getRemainingTokens(tokenType) > 0)
            return true;

        return false;
    }
}
