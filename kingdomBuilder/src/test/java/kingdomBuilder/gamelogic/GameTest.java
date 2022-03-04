package kingdomBuilder.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    String first =
            "FORREST;FORREST;FLOWER;DESERT;DESERT;DESERT;DESERT;CANYON;DESERT;DESERT;" +
            "FORREST;MOUNTAIN;FLOWER;FLOWER;DESERT;CANYON;DESERT;CANYON;CANYON;CANYON;" +
            "FORREST;FORREST;FORREST;FLOWER;DESERT;DESERT;CANYON;TOWER;WATER;CANYON;" +
            "FORREST;FORREST;FLOWER;FLOWER;DESERT;DESERT;DESERT;DESERT;WATER;WATER;" +
            "MOUNTAIN;FLOWER;FLOWER;WATER;FLOWER;DESERT;DESERT;WATER;WATER;FORREST;" +
            "MOUNTAIN;GRAS;FLOWER;TOWER;WATER;WATER;WATER;FLOWER;FORREST;FORREST;" +
            "GRAS;MOUNTAIN;GRAS;GRAS;GRAS;WATER;FLOWER;FLOWER;FORREST;FORREST;" +
            "MOUNTAIN;MOUNTAIN;GRAS;WATER;WATER;CANYON;CASTLE;FLOWER;FLOWER;GRAS;" +
            "CANYON;MOUNTAIN;WATER;MOUNTAIN;CANYON;GRAS;GRAS;GRAS;GRAS;GRAS;" +
            "CANYON;CANYON;MOUNTAIN;MOUNTAIN;CANYON;CANYON;CANYON;GRAS;GRAS;GRAS";

    String second =
            "DESERT;DESERT;DESERT;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
            "DESERT;CANYON;DESERT;WATER;WATER;FORREST;FORREST;CASTLE;WATER;WATER;" +
            "CANYON;WATER;WATER;WATER;WATER;FORREST;CANYON;CANYON;CANYON;WATER;" +
            "WATER;FLOWER;FLOWER;FLOWER;WATER;WATER;FORREST;FLOWER;FLOWER;WATER;" +
            "WATER;FLOWER;FLOWER;GRAS;GRAS;GRAS;WATER;WATER;WATER;WATER;" +
            "FORREST;FORREST;FORREST;FORREST;GRAS;GRAS;GRAS;OASIS;WATER;WATER;" +
            "FORREST;FORREST;FORREST;FLOWER;GRAS;CANYON;CANYON;DESERT;WATER;WATER;" +
            "GRAS;FORREST;OASIS;FLOWER;GRAS;CANYON;CANYON;DESERT;DESERT;WATER;" +
            "GRAS;GRAS;FLOWER;FLOWER;FLOWER;DESERT;DESERT;CANYON;DESERT;WATER;" +
            "GRAS;GRAS;GRAS;FLOWER;FLOWER;CANYON;CANYON;WATER;WATER;WATER";

    String third = "FLOWER;FLOWER;FLOWER;WATER;FLOWER;FLOWER;DESERT;DESERT;DESERT;DESERT;DESERT;FLOWER;FLOWER;" +
            "WATER;FLOWER;CANYON;FLOWER;DESERT;DESERT;DESERT;DESERT;DESERT;FLOWER;FLOWER;WATER;CANYON;TAVERN;CANYON;" +
            "DESERT;WATER;MOUNTAIN;DESERT;FLOWER;CASTLE;WATER;WATER;CANYON;WATER;WATER;WATER;MOUNTAIN;DESERT;FLOWER;" +
            "GRAS;GRAS;GRAS;WATER;FORREST;FORREST;FORREST;DESERT;MOUNTAIN;FLOWER;GRAS;GRAS;FORREST;FORREST;FORREST;" +
            "FORREST;FORREST;DESERT;MOUNTAIN;FLOWER;FORREST;GRAS;FORREST;FORREST;GRAS;FORREST;FORREST;CANYON;CANYON;" +
            "MOUNTAIN;FORREST;FORREST;CANYON;TAVERN;GRAS;GRAS;GRAS;CANYON;CANYON;MOUNTAIN;MOUNTAIN;FORREST;CANYON;" +
            "CANYON;GRAS;GRAS;GRAS;CANYON;CANYON;MOUNTAIN;MOUNTAIN;CANYON;CANYON;GRAS;GRAS;GRAS;GRAS";

    String fourth = "CANYON;MOUNTAIN;MOUNTAIN;MOUNTAIN;CANYON;CANYON;CANYON;GRAS;GRAS;GRAS;CANYON;MOUNTAIN;" +
            "MOUNTAIN;CANYON;CANYON;FORREST;PADDOCK;GRAS;GRAS;GRAS;CANYON;CANYON;CANYON;MOUNTAIN;FORREST;FORREST;" +
            "FORREST;FORREST;FORREST;FORREST;DESERT;DESERT;MOUNTAIN;MOUNTAIN;FORREST;WATER;FORREST;WATER;FORREST;" +
            "FORREST;DESERT;DESERT;MOUNTAIN;WATER;WATER;CANYON;WATER;GRAS;WATER;WATER;WATER;WATER;WATER;MOUNTAIN;" +
            "MOUNTAIN;CANYON;FLOWER;CASTLE;GRAS;GRAS;DESERT;DESERT;DESERT;DESERT;MOUNTAIN;CANYON;FLOWER;GRAS;GRAS;" +
            "GRAS;DESERT;DESERT;DESERT;FLOWER;CANYON;MOUNTAIN;FLOWER;FLOWER;GRAS;GRAS;DESERT;DESERT;PADDOCK;FLOWER;" +
            "FLOWER;FLOWER;FLOWER;GRAS;GRAS;FORREST;DESERT;DESERT;FLOWER;FLOWER;FLOWER;FLOWER;FLOWER;FORREST;" +
            "FORREST;FORREST";

    TileType[] quadrant1;
    TileType[] quadrant2;
    TileType[] quadrant3;
    TileType[] quadrant4;

    GameMap gameMap;

    Player playerOne;
    Player playerTwo;

    @BeforeEach
    public void testInitializeMap() {
        quadrant1 = Arrays.stream(first.split(";")).map(TileType::valueOf).toArray(TileType[]::new);
        quadrant2 = Arrays.stream(second.split(";")).map(TileType::valueOf).toArray(TileType[]::new);
        quadrant3 = Arrays.stream(third.split(";")).map(TileType::valueOf).toArray(TileType[]::new);
        quadrant4 = Arrays.stream(fourth.split(";")).map(TileType::valueOf).toArray(TileType[]::new);

        gameMap = new GameMap(2, quadrant1, quadrant2, quadrant3, quadrant4);
    }

    @BeforeEach
    public void testInitializePlayers() {
        playerOne = new Player(0, "TestPlayer1", PlayerColor.RED, 40);
        playerTwo = new Player(1, "TestPlayer2", PlayerColor.BLUE, 40);
    }

    @Test
    void testUnsafePlaceSettlement() {
        Game.unsafePlaceSettlement(gameMap, playerOne, 5, 8);
        assertSame(playerOne, gameMap.at(5, 8).occupiedBy);
    }

    @Test
    void testUnsafeMoveSettlement() {
        // place a settlement and move it
        // check if the old tile is empty and the new one occupied
        gameMap.at(7,6).placeSettlement(playerOne);
        Game.unsafeMoveSettlement(gameMap, playerOne, 7, 6, 11, 7);

        assertSame(null, gameMap.at(7,6).occupiedBy);
        assertSame(playerOne, gameMap.at(11,7).occupiedBy);
    }

    @Test
    void testScoreCastles() {
        // just checking the coordinates of the castles
        assertSame(TileType.CASTLE, gameMap.at(7, 6).tileType);
        assertSame(TileType.CASTLE, gameMap.at(11, 7).tileType);

        //no settlement placed for playerOne
        assertEquals(0, Game.scoreCastles(gameMap, playerOne));

        //one settlement next to a castle
        gameMap.at(8,6).placeSettlement(playerOne);
        assertEquals(3, Game.scoreCastles(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();

        //two settlements next to the same castle
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(7,7).placeSettlement(playerOne);
        assertEquals(3, Game.scoreCastles(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(7,7).removeSettlement();

        //two settlements next to different castles
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(11,6).placeSettlement(playerOne);
        assertEquals(6, Game.scoreCastles(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(11,6).removeSettlement();

        //two players next to the same castle
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(7,7).placeSettlement(playerTwo);
        assertEquals(3, Game.scoreCastles(gameMap, playerOne));
        assertEquals(3, Game.scoreCastles(gameMap, playerTwo));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(7,7).removeSettlement();

        //two players next to different castles
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(11,6).placeSettlement(playerTwo);
        assertEquals(3, Game.scoreCastles(gameMap, playerOne));
        assertEquals(3, Game.scoreCastles(gameMap, playerTwo));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(11,6).removeSettlement();
    }

    @Test
    void testScoreAnchorite() {
        //no settlement placed
        assertEquals(0, Game.scoreAnchorite(gameMap,playerOne));

        //one settlement placed
        gameMap.at(8,6).placeSettlement(playerOne);
        assertEquals(1, Game.scoreAnchorite(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();

        //one small group
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerOne);
        gameMap.at(8,7).placeSettlement(playerOne);
        gameMap.at(7,7).placeSettlement(playerOne);
        assertEquals(1, Game.scoreAnchorite(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();
        gameMap.at(8,7).removeSettlement();
        gameMap.at(7,7).removeSettlement();

        //three incoherent groups
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(6,14).placeSettlement(playerOne);
        gameMap.at(16,7).placeSettlement(playerOne);
        assertEquals(3, Game.scoreAnchorite(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(6,14).removeSettlement();
        gameMap.at(16,7).removeSettlement();

        //two players, two 1-settlement-groups
        gameMap.at(6,14).placeSettlement(playerOne);
        gameMap.at(16,7).placeSettlement(playerTwo);
        assertEquals(1, Game.scoreAnchorite(gameMap,playerOne));
        gameMap.at(6,14).removeSettlement();
        gameMap.at(16,7).removeSettlement();
    }

    @Test
    void testScoreCitizen() {
        //no settlement placed
        assertEquals(0, Game.scoreCitizen(gameMap,playerOne));

        //one settlement placed
        gameMap.at(8,6).placeSettlement(playerOne);
        assertEquals(0, Game.scoreCitizen(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();

        //two settlement group
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerOne);
        assertEquals(1, Game.scoreCitizen(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();

        //one small group
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerOne);
        gameMap.at(8,7).placeSettlement(playerOne);
        gameMap.at(7,7).placeSettlement(playerOne);
        assertEquals(2, Game.scoreCitizen(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();
        gameMap.at(8,7).removeSettlement();
        gameMap.at(7,7).removeSettlement();

        //two incoherent groups with 2 settlements each
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerOne);
        gameMap.at(6,14).placeSettlement(playerOne);
        gameMap.at(7,14).placeSettlement(playerOne);
        assertEquals(1, Game.scoreCitizen(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();
        gameMap.at(6,14).removeSettlement();
        gameMap.at(7,14).removeSettlement();

        //two incoherent groups with different sizes
        //one group with 1 settlement - the other with 2 settlements
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(6,14).placeSettlement(playerOne);
        gameMap.at(7,14).placeSettlement(playerOne);
        assertEquals(1, Game.scoreCitizen(gameMap,playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(6,14).removeSettlement();
        gameMap.at(7,14).removeSettlement();

        //two players, two 2-settlement-groups
        gameMap.at(6,14).placeSettlement(playerOne);
        gameMap.at(5,13).placeSettlement(playerOne);
        gameMap.at(7,14).placeSettlement(playerTwo);
        gameMap.at(7,13).placeSettlement(playerTwo);
        assertEquals(1, Game.scoreCitizen(gameMap,playerOne));
        gameMap.at(6,14).removeSettlement();
        gameMap.at(5,13).removeSettlement();
        gameMap.at(7,14).removeSettlement();
        gameMap.at(7,13).removeSettlement();
    }

    @Test
    void testScoreExplorer() {
        //no settlement placed for playerOne
        assertEquals(0, Game.scoreExplorer(gameMap, playerOne));

        // only one line with one house for playerOne
        gameMap.at(8,6).placeSettlement(playerOne);
        assertEquals(1, Game.scoreExplorer(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();

        // 2 lines with each one house for playerOne
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(8,7).placeSettlement(playerOne);
        assertEquals(2, Game.scoreExplorer(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(8,7).removeSettlement();

        // 2 lines with 1 line with one house and 1 line with two houses for playerOne
        gameMap.at(8,7).placeSettlement(playerOne);
        gameMap.at(8,8).placeSettlement(playerOne);
        gameMap.at(9,7).placeSettlement(playerOne);
        assertEquals(2, Game.scoreExplorer(gameMap, playerOne));
        gameMap.at(8,7).removeSettlement();
        gameMap.at(8,8).removeSettlement();
        gameMap.at(9,7).removeSettlement();

        // 1 line with one house for playerOne
        // another player makes a turn in another line
        gameMap.at(6,14).placeSettlement(playerTwo);
        gameMap.at(8,8).placeSettlement(playerOne);
        assertEquals(1, Game.scoreExplorer(gameMap, playerOne));
        gameMap.at(6,14).removeSettlement();
        gameMap.at(8,8).removeSettlement();

        // 1 line with one house for playerOne
        // another player makes a turn in the same line
        gameMap.at(9,8).placeSettlement(playerTwo);
        gameMap.at(8,8).placeSettlement(playerOne);
        assertEquals(1, Game.scoreExplorer(gameMap, playerOne));
        gameMap.at(9,8).removeSettlement();
        gameMap.at(8,8).removeSettlement();
    }

    @Test
    void testScoreKnight() {
        //no settlement placed for playerOne
        assertEquals(0, Game.scoreKnight(gameMap, playerOne));

        //1 settlement placed for playerOne
        gameMap.at(8,6).placeSettlement(playerOne);
        assertEquals(2, Game.scoreKnight(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();

        //2 settlements in different lines for player One
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(8,7).placeSettlement(playerOne);
        assertEquals(2, Game.scoreKnight(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(8,7).removeSettlement();

        //2 settlements in the same line
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerOne);
        assertEquals(4, Game.scoreKnight(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();

        //first one line with 1 settlement
        //after that one line with 2 settlements
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerOne);
        assertEquals(4, Game.scoreKnight(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();

        //first one line with 2 settlement
        //after that one line with 1 settlements
        gameMap.at(19,19).placeSettlement(playerOne);
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerOne);
        assertEquals(4, Game.scoreKnight(gameMap, playerOne));
        gameMap.at(19,19).removeSettlement();
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();

        //1 settlement placed for playerOne
        //1 settlement placed in the same line for playerTwo
        gameMap.at(8,6).placeSettlement(playerOne);
        gameMap.at(9,6).placeSettlement(playerTwo);
        assertEquals(2, Game.scoreKnight(gameMap, playerOne));
        gameMap.at(8,6).removeSettlement();
        gameMap.at(9,6).removeSettlement();

        //first one line with 1 settlement
        //after another line with 1 settlements
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(8,6).placeSettlement(playerOne);
        assertEquals(2, Game.scoreKnight(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();
        gameMap.at(8,6).removeSettlement();

    }

    @Test
    void testScoreLord() {

        Player playerThird = new Player(3, "TestPlayer3", PlayerColor.BLACK, 40);

        //Test#1: no settlement placed
        assertEquals(0, Game.scoreLord(gameMap, playerOne));

        //Test#2: one settlement in upper left quadrant (largest group)
        gameMap.at(0,0).placeSettlement(playerOne);
        assertEquals(12, Game.scoreLord(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();

        //Test#3: biggest group in upper left quadrant and biggest in bottom right quadrant
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(19,19).placeSettlement(playerOne);

        assertEquals(24, Game.scoreLord(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();
        gameMap.at(19,19).removeSettlement();

        //Test#4: second-largest group in upper left quadrant for playerOne
        gameMap.at(0,0).placeSettlement(playerTwo);
        gameMap.at(1,0).placeSettlement(playerTwo);
        gameMap.at(0,1).placeSettlement(playerOne);

        assertEquals(6, Game.scoreLord(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();
        gameMap.at(1,0).removeSettlement();
        gameMap.at(0,1).removeSettlement();

        //Test#5: at quadrant border
        //upper left is the biggest
        //upper right is second biggest
        gameMap.at(9,0).placeSettlement(playerOne);
        gameMap.at(10,0).placeSettlement(playerOne);

        gameMap.at(19,0).placeSettlement(playerTwo);
        gameMap.at(19,1).placeSettlement(playerTwo);

        assertEquals(18, Game.scoreLord(gameMap, playerOne));

        //Test#6: third-largest group
        //(same map as #5)
        gameMap.at(15,0).placeSettlement(playerThird);
        assertEquals(0, Game.scoreLord(gameMap, playerThird));

        gameMap.at(9,0).removeSettlement();
        gameMap.at(10,0).removeSettlement();
        gameMap.at(19,0).removeSettlement();
        gameMap.at(19,1).removeSettlement();
        gameMap.at(15,0).removeSettlement();

        //Test#7: two players with same amount of settlements
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(1,0).placeSettlement(playerOne);
        gameMap.at(19,0).placeSettlement(playerTwo);
        gameMap.at(19,1).placeSettlement(playerTwo);

        assertEquals(12, Game.scoreLord(gameMap, playerOne));
        assertEquals(12, Game.scoreLord(gameMap, playerTwo));

        gameMap.at(0,0).removeSettlement();
        gameMap.at(1,0).removeSettlement();
        gameMap.at(19,0).removeSettlement();
        gameMap.at(19,1).removeSettlement();
    }
}