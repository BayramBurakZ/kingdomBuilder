package kingdomBuilder.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;
    Tile tokenOracle;
    Tile tokenOracle2;
    Tile tokenHarbor;
    Tile tokenPaddock;
    Tile tokenOasis;
    Tile tokenTavern;
    Tile tokenBarn;
    Tile tokenTower;
    Tile tokenFarm;

    @BeforeEach
    public void testInitialize() {
        player = new Player(0, "TestPlayer", PlayerColor.RED, 40);

        tokenOracle = new Tile(4, 4, TileType.ORACLE, 2, 20);
        tokenOracle2 = new Tile(8, 8, TileType.ORACLE, 2, 20);
        tokenHarbor = new Tile(12, 12, TileType.HARBOR, 2, 20);
        tokenPaddock = new Tile(13, 4, TileType.PADDOCK, 2, 20);
        tokenOasis = new Tile(14, 4, TileType.OASIS, 2, 20);
        tokenTavern = new Tile(4, 14, TileType.TAVERN, 2, 20);
        tokenBarn = new Tile(4, 8, TileType.BARN, 2, 20);
        tokenTower = new Tile(4, 15, TileType.TOWER, 2, 20);
        tokenFarm = new Tile(4, 18, TileType.FARM, 2, 20);

        assertSame(0, player.ID);
        assertSame("TestPlayer", player.name);
        assertSame(PlayerColor.RED, player.color);
        assertSame(40, player.getRemainingSettlements());
        assertSame(null, player.getTerrainCard());
    }

    @Test
    void testStartTurn() {
        player.startTurn();

        assertSame(3, player.getRemainingSettlementsOfTurn());
        assertSame(player.getRemainingSettlementsOfTurn(), player.remainingSettlementsOfTurn);

        assertSame(TurnState.START_OF_TURN, player.getCurrentTurnState());

        // Tokens
        player.addToken(tokenOracle);
        player.startTurn();

        assertSame(1, player.getTokens().get(TileType.ORACLE).getRemaining());

        player.addToken(tokenOracle2);
        player.startTurn();

        assertSame(2, player.getTokens().get(TileType.ORACLE).getRemaining());

        player.addToken(tokenHarbor);
        player.startTurn();

        assertSame(2, player.getTokens().get(TileType.ORACLE).getRemaining());
        assertSame(1, player.getTokens().get(TileType.HARBOR).getRemaining());

        // settlement left <3
        Player p = new Player(1, "TestPlayer", PlayerColor.RED, 2);

        p.startTurn();
        assertSame(2, p.getRemainingSettlementsOfTurn());
        assertSame(p.getRemainingSettlementsOfTurn(), p.remainingSettlementsOfTurn);
    }

    @Test
    void testSetTerrainCard() {
        assertSame(null, player.getTerrainCard());

        player.setTerrainCard(TileType.FORREST);
        assertSame(TileType.FORREST, player.getTerrainCard());

        player.setTerrainCard(TileType.DESERT);
        assertSame(TileType.DESERT, player.getTerrainCard());

        player.setTerrainCard(TileType.GRAS);
        assertSame(TileType.GRAS, player.getTerrainCard());

        player.setTerrainCard(TileType.CANYON);
        assertSame(TileType.CANYON, player.getTerrainCard());

        player.setTerrainCard(TileType.FLOWER);
        assertSame(TileType.FLOWER, player.getTerrainCard());
    }

    @Test
    void testAddToken() {
        for (var x : player.getTokens().values()) {
            assertEquals(0, x.getTotal());
        }

        player.addToken(tokenHarbor);
        player.addToken(tokenBarn);
        player.addToken(tokenOracle);
        player.addToken(tokenOasis);
        player.addToken(tokenFarm);
        player.addToken(tokenTavern);
        player.addToken(tokenTower);
        player.addToken(tokenPaddock);

        for (var x : player.getTokens().values()) {
            assertEquals(1, x.getTotal());
        }

        // do not add tokens from same Tile
        player.addToken(tokenHarbor);
        player.addToken(tokenBarn);
        player.addToken(tokenOracle);
        player.addToken(tokenOasis);
        player.addToken(tokenFarm);
        player.addToken(tokenTavern);
        player.addToken(tokenTower);
        player.addToken(tokenPaddock);

        for (var x : player.getTokens().values()) {
            assertEquals(1, x.getTotal());
        }

        player.addToken(tokenOracle2);
        assertEquals(2, player.getTokens().get(TileType.ORACLE).getTotal());
    }

    @Test
    void testHasTokenFrom() {
        assertFalse(player.hasTokenFrom(tokenTower));
        player.addToken(tokenTower);
        assertTrue(player.hasTokenFrom(tokenTower));
    }

    @Test
    void testRemoveToken() {
        assertSame(0, player.getTokens().get(TileType.TOWER).getTotal());
        player.addToken(tokenTower);

        assertSame(1, player.getTokens().get(TileType.TOWER).getTotal());
        player.removeToken(tokenTower);

        assertSame(0, player.getTokens().get(TileType.TOWER).getTotal());
    }

    @Test
    void testUseToken() {
        assertSame(0, player.getTokens().get(TileType.TOWER).getTotal());
        player.addToken(tokenTower);
        player.startTurn();
        assertSame(1, player.getTokens().get(TileType.TOWER).getRemaining());

        player.useToken(TileType.TOWER);
        assertSame(0, player.getTokens().get(TileType.TOWER).getRemaining());
    }

    @Test
    void testUndoToken() {
        assertSame(0, player.getTokens().get(TileType.TOWER).getTotal());
        player.addToken(tokenTower);
        player.startTurn();
        assertSame(1, player.getTokens().get(TileType.TOWER).getRemaining());

        player.useToken(TileType.TOWER);
        assertSame(0, player.getTokens().get(TileType.TOWER).getRemaining());

        player.undoToken(TileType.TOWER);
        assertSame(1, player.getTokens().get(TileType.TOWER).getRemaining());
    }

    @Test
    void testUseBasicTurn() {
        player.startTurn();
        assertSame(TurnState.START_OF_TURN, player.getCurrentTurnState());
        assertSame(3, player.getRemainingSettlementsOfTurn());

        player.useBasicTurn();
        assertSame(TurnState.BASIC_TURN, player.getCurrentTurnState());
        assertSame(2, player.getRemainingSettlementsOfTurn());

        player.useBasicTurn();
        player.useBasicTurn();
        assertSame(TurnState.END_OF_TURN, player.getCurrentTurnState());
        assertSame(0, player.getRemainingSettlementsOfTurn());
    }

    @Test
    void testDecrementRemainingSettlements() {
        player.startTurn();
        assertSame(40, player.getRemainingSettlements());
        assertSame(3, player.getRemainingSettlementsOfTurn());

        player.decrementRemainingSettlements();
        assertSame(39, player.getRemainingSettlements());
        assertSame(3, player.getRemainingSettlementsOfTurn());

        Player p = new Player(1, "TestPlayer", PlayerColor.RED, 2);
        p.startTurn();
        p.decrementRemainingSettlements();
        assertSame(1, p.getRemainingSettlementsOfTurn());

        p.decrementRemainingSettlements();
        assertSame(0, p.getRemainingSettlements());
        assertSame(0, p.getRemainingSettlementsOfTurn());
        assertSame(TurnState.END_OF_TURN, p.getCurrentTurnState());

    }

    @Test
    void testHasRemainingSettlements() {
        assertTrue(player.hasRemainingSettlements());

        Player p = new Player(1, "TestPlayer", PlayerColor.RED, 1);
        p.startTurn();
        p.decrementRemainingSettlements();

        assertFalse(p.hasRemainingSettlements());
    }

    @Test
    void testGetRemainingSettlements() {
        int remainingSettlements = 10;
        Player p = new Player(1, "TestPlayer", PlayerColor.RED, remainingSettlements);
        p.startTurn();

        assertSame(remainingSettlements, p.getRemainingSettlements());
    }

    @Test
    void testGetTerrainCard() {
        TileType t = TileType.DESERT;
        player.setTerrainCard(t);
        assertSame(t, player.getTerrainCard());
    }

    @Test
    void testPlayerHasTokenLeft() {
        player.startTurn();
        TileType t = TileType.HARBOR;
        assertFalse(player.playerHasTokenLeft(t));

        player.addToken(tokenHarbor);
        player.startTurn();
        assertTrue(player.playerHasTokenLeft(t));
    }

    @Test
    void testGetRemainingSettlementsOfTurn() {
        player.startTurn();
        assertSame(3, player.getRemainingSettlementsOfTurn());

        player.useBasicTurn();
        assertSame(2, player.getRemainingSettlementsOfTurn());
    }

    @Test
    void testGetTokens() {
        assertSame(8, player.getTokens().size());

        for (var x : player.getTokens().values()) {
            assertEquals(0, x.getTotal());
        }

        player.addToken(tokenHarbor);
        player.addToken(tokenBarn);
        player.addToken(tokenOracle);
        player.addToken(tokenOasis);
        player.addToken(tokenFarm);
        player.addToken(tokenTavern);
        player.addToken(tokenTower);
        player.addToken(tokenPaddock);

        for (var x : player.getTokens().values()) {
            assertEquals(1, x.getTotal());
        }
    }

    @Test
    void testGetCurrentTurnState() {
        player.startTurn();
        assertSame(TurnState.START_OF_TURN, player.getCurrentTurnState());

        player.useBasicTurn();
        assertSame(TurnState.BASIC_TURN, player.getCurrentTurnState());

        player.useBasicTurn();
        player.useBasicTurn();
        assertSame(TurnState.END_OF_TURN, player.getCurrentTurnState());

    }
}