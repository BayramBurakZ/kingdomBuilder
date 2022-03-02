package kingdomBuilder.gamelogic;

import java.util.HashMap;
import java.util.HashSet;

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
     * Represents the settlements that the player needed to place at the start of their turn.
     */
    int remainingSettlementsAtStartOfTurn;

    // TODO: maybe properly hide this class instead of moving it outside
    /**
     * Class that represents a token.
     */
    public static class Token {
        private final HashSet<Tile> originTiles = new HashSet<>();
        private int remaining;

        public int getRemaining() { return remaining; }
        public int getTotal() {
            return originTiles.size();
        }
    }

    /**
     * Constructs a new player object with the given information.
     *
     * @param ID               the client id.
     * @param name             the name of the client.
     * @param color            the color of the client.
     * @param totalSettlements the amount of settlement that the player has.
     */
    public Player(int ID, String name, PlayerColor color, int totalSettlements) {
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.remainingSettlements = totalSettlements;
        this.tokens = new HashMap<>(TileType.tokenType.size());
        TileType.tokenType.forEach(tileType -> tokens.put(tileType, new Token()));
    }

    /**
     * Updates the information for the player at the start of a turn.
     */
    public void startTurn() {
        remainingSettlementsOfTurn = Math.min(remainingSettlements, Game.SETTLEMENTS_PER_TURN);
        remainingSettlementsAtStartOfTurn = remainingSettlementsOfTurn;
        tokens.forEach((tileType, token) -> token.remaining = token.getTotal());
    }

    // TODO: make it better
    //  Problem here: more than one Terrain Card per Turn
    public void setTerrainCard(TileType tileType) {
        this.terrainCard = tileType;
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

        if (!TileType.tokenType.contains(originTile.tileType))
            throw new RuntimeException("The tile's type is not a token type!");

        Token token = tokens.get(originTile.tileType);

        if (originTile.hasTokens() && !token.originTiles.contains(originTile)) {
            token.originTiles.add(originTile);
            originTile.takeTokenFromSpecialPlace();
        }
    }

    /**
     * Returns whether the player has a token from the specified special place.
     * @param originTile the special place the player might own a token from.
     * @return whether the player has a token from the specified special place.
     */
    public boolean hasTokenFrom(Tile originTile) {
        if (!TileType.tokenType.contains(originTile.tileType))
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
        if (!TileType.tokenType.contains(originTile.tileType))
            throw new RuntimeException("The tile's type is not a token type!");

        Token token = tokens.get(originTile.tileType);

        //if (!token.originTiles.contains(originTile))
        //    throw new RuntimeException("The player doesn't own a token from this tile!");

        token.originTiles.remove(originTile);
    }

    /**
     * Uses the given token by reducing the amount of remaining uses for the current turn.
     *
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
     * @return true if the player has settlements left for the basic turn.
     */
    public boolean hasRemainingSettlements() {
        return remainingSettlements > 0;
    }

    /**
     * Represents the remaining settlements of this player.
     *
     * @return the amount of settlements the player needs to place for the basic turn.
     */
    public int getRemainingSettlements() {
        return remainingSettlements;
    }

    /**
     * Represents the terrain card for this player.
     *
     * @return the TileType of the terrain card of the player.
     */
    public TileType getTerrainCard() {
        return terrainCard;
    }

    /**
     * Check if player owns a specific type of token.
     *
     * @param tokenType the token type to check.
     * @return true if he owns at least one of that token.
     */
    public boolean playerHasTokenLeft(TileType tokenType) {
        return tokens.get(tokenType).getRemaining() > 0;
    }

    /**
     * Get the remaining settlements of a turn.
     *
     * @return the remaining settlements.
     */
    public int getRemainingSettlementsOfTurn() {
        return remainingSettlementsOfTurn;
    }

    // TODO: read-only map or iterator
    public HashMap<TileType, Token> getTokens() {
        return tokens;
    }
}