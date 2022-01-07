package kingdomBuilder.gamelogic;

import java.util.HashMap;

import static kingdomBuilder.gamelogic.Game.*;

/**
 * Contains the data of a player.
 */
public class Player {

    public final int ID;
    public final String name;
    public final PlayerColor color;
    public int remainingSettlements;
    private final HashMap<TileType, Token> tokens;

    // data of the player's current/next turn
    public TileType terrainCard;
    public int remainingSettlementsOfTurn;

    private static class Token {
        int total;
        int remaining;
    }

    public Player(int ID, String name, PlayerColor color, int totalSettlements) {
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.remainingSettlements = totalSettlements;
        this.tokens = new HashMap<>(Game.tokenType.size());
        tokenType.forEach(tileType -> tokens.put(tileType, new Token()));
    }

    public void startTurn(TileType terrainCard) {
        this.terrainCard = terrainCard;
        remainingSettlementsOfTurn = Math.min(remainingSettlements, Game.SETTLEMENTS_PER_TURN);
        tokens.forEach((tileType, token) -> token.remaining = token.total);
    }

    public void endTurn() {
        // placeholder, in case it's needed later
    }

    public void addToken(TileType tokenType) {
        tokens.get(tokenType).total++;
    }

    public void removeToken(TileType tokenType) {
        Token token = tokens.get(tokenType);
        if (token.total <= 0)
            throw new RuntimeException("Tried to remove a token but the player had none!");
        token.total--;
    }

    public int remainingToken(TileType tokenType) {
        return tokens.get(tokenType).remaining;
    }

    public void useToken(TileType tokenType) {
        Token token = tokens.get(tokenType);
        if (token.remaining <= 0)
            throw new RuntimeException("Tried to use a token but the player had none remaining!");
        token.remaining--;
    }
}
