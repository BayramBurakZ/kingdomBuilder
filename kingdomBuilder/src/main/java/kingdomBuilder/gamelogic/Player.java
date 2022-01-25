package kingdomBuilder.gamelogic;

import java.util.HashMap;

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
     * Represents the remaining settlements of this player.
     */
    public int remainingSettlements;

    /**
     * Represents the tokens that this player has.
     */
    private final HashMap<TileType, Token> tokens;

    // data of the player's current/next turn
    /**
     * Represents the terrain card for this player.
     */
    public TileType terrainCard;

    /**
     * Represents the settlements that the player needs to place in this turn.
     */
    public int remainingSettlementsOfTurn;

    // TODO: JavaDoc!
    private static class Token {
        int total;
        int remaining;
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
    public void startTurn(TileType terrainCard) {
        this.terrainCard = terrainCard;
        remainingSettlementsOfTurn = Math.min(remainingSettlements, Game.SETTLEMENTS_PER_TURN);
        tokens.forEach((tileType, token) -> token.remaining = token.total);
    }

    public void endTurn() {
        // placeholder, in case it's needed later
    }

    /**
     * Gives the player a token.
     *
     * @param tokenType the token type that is given to the player.
     */
    public void addToken(TileType tokenType) {
        tokens.get(tokenType).total++;
    }

    /**
     * Removes a token from the player.
     *
     * @param tokenType the token type that is removed.
     */
    public void removeToken(TileType tokenType) {
        Token token = tokens.get(tokenType);
        if (token.total <= 0)
            throw new RuntimeException("Tried to remove a token but the player had none!");
        token.total--;
    }

    /**
     * Returns the amount of tokens from a type the player has.
     *
     * @param tokenType the token type to check.
     * @return The amount of that type.
     */
    public int remainingToken(TileType tokenType) {
        return tokens.get(tokenType).remaining;
    }


    /**
     * Check if player owns a specific type of token
     *
     * @param tokenType the token type to check
     * @return true if he ownes at least one of that token
     */
    public boolean playerHasTokenLeft(TileType tokenType) {
        if (remainingToken(tokenType) > 0)
            return true;

        return false;
    }

    /**
     * Uses the given token.
     * @param tokenType the type of the token to use it.
     */
    public void useToken(TileType tokenType) {
        //TODO: this function exists twice? @removeToken
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
}
