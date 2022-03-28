package kingdomBuilder.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

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
    Player playerThree;

    @BeforeEach
    public void testInitializeMap() {
        playerOne = new Player(0, "TestPlayer1", PlayerColor.RED, 40);
        playerTwo = new Player(1, "TestPlayer2", PlayerColor.BLUE, 40);
        playerThree = new Player(2, "TestPlayer3", PlayerColor.BLACK, 40);

        quadrant1 = Arrays.stream(first.split(";")).map(TileType::valueOf).toArray(TileType[]::new);
        quadrant2 = Arrays.stream(second.split(";")).map(TileType::valueOf).toArray(TileType[]::new);
        quadrant3 = Arrays.stream(third.split(";")).map(TileType::valueOf).toArray(TileType[]::new);
        quadrant4 = Arrays.stream(fourth.split(";")).map(TileType::valueOf).toArray(TileType[]::new);

        gameMap = new GameMap(2, quadrant1, quadrant2, quadrant3, quadrant4);
    }


    @Test
    void testIsBlocked() {

        // Test1: Check if Tile is not blocked.
        assertFalse(gameMap.at(8,6).isBlocked());

        // Test2: Check if tile is blocked.
        gameMap.at(8, 6).placeSettlement(playerOne);
        assertTrue( gameMap.at(8, 6).isBlocked());
        gameMap.at(8, 6).removeSettlement();

        // Test3: Check if tile is not blocked again.
        assertFalse( gameMap.at(8, 6).isBlocked());

        // Test4: Check for token (tower)
        assertTrue( gameMap.at(5, 3).isBlocked());

        // Test5: Check for token (tavern)
        assertTrue( gameMap.at(7, 16).isBlocked());

        // Test6: Check for castle.
        assertTrue( gameMap.at(15, 17).isBlocked());

        // Test7: Check for water.
        assertTrue( gameMap.at(10, 9).isBlocked());

        // Test8: Check for mountain.
        assertTrue( gameMap.at(11, 12).isBlocked());

    }

    @Test
    void testOccupiedBy() {

        // Test1: occupied by nobody
        assertNull(gameMap.at(5,19).occupiedBy());

        // Test2: occupied by playerOne
        gameMap.at(17, 17).placeSettlement(playerOne);
        assertEquals(playerOne, gameMap.at(17,17).occupiedBy);
        gameMap.at(17, 17).removeSettlement();

        // Test3: occupied by nobody again
        assertNull(gameMap.at(17, 17).occupiedBy);

    }

    @Test
    void testIsAtBorder() {

        // Test1: corners.
        assertTrue(gameMap.at(0,0).isAtBorder(gameMap));
        assertTrue(gameMap.at(0,19).isAtBorder(gameMap));
        assertTrue(gameMap.at(19,0).isAtBorder(gameMap));
        assertTrue(gameMap.at(19,19).isAtBorder(gameMap));

        // Test2: sides.
        assertTrue(gameMap.at(0,10).isAtBorder(gameMap));
        assertTrue(gameMap.at(10,19).isAtBorder(gameMap));
        assertTrue(gameMap.at(10,0).isAtBorder(gameMap));
        assertTrue(gameMap.at(19,10).isAtBorder(gameMap));

        // Test3: not at borders.
        assertFalse(gameMap.at(5,4).isAtBorder(gameMap));
        assertFalse(gameMap.at(13,12).isAtBorder(gameMap));
        assertFalse(gameMap.at(10,4).isAtBorder(gameMap));
        assertFalse(gameMap.at(8,8).isAtBorder(gameMap));
    }

    @Test
    void testHasTokens() {

        // Test1: normal tile has no tokens
        Exception exception = assertThrows(RuntimeException.class, () -> {gameMap.at(10,1).hasTokens();});
        assertEquals("The tile is not a special place!", exception.getMessage());

        // Test2: castle tile has no tokens.
        exception = assertThrows(RuntimeException.class, () -> {gameMap.at(7,6).hasTokens();});
        assertEquals("The tile is not a special place!", exception.getMessage());

        // Test3: Token tile has tokens left.
        assertTrue(gameMap.at(15,7).hasTokens());

        //TODO: more tests.

    }

    @Test
    void testIsAtEndOfAChain() {

        // Horizontal chain
        gameMap.at(6,9).placeSettlement(playerOne);
        gameMap.at(7,9).placeSettlement(playerOne);
        gameMap.at(8,9).placeSettlement(playerOne);

        // Test1: Tile above and under is not a part of the chain.
        assertFalse(gameMap.at(7,10).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(7,8).isAtEndOfAChain(gameMap, playerOne));

        // Test2: Left end of the chain for playerOne but not for playerTwo
        assertTrue(gameMap.at(5,9).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(5,9).isAtEndOfAChain(gameMap, playerTwo));

        // Test3: Right end of the chain for playerOne but not for playerTwo
        assertTrue(gameMap.at(9,9).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(9,9).isAtEndOfAChain(gameMap, playerTwo));

        gameMap.at(6,9).removeSettlement();
        gameMap.at(7,9).removeSettlement();
        gameMap.at(8,9).removeSettlement();


        // Right diagonal chain.
        gameMap.at(7,7).placeSettlement(playerOne);
        gameMap.at(7,8).placeSettlement(playerOne);
        gameMap.at(6,9).placeSettlement(playerOne);

        // Test4: Tile left and right is not a part of the chain
        assertFalse(gameMap.at(8,8).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(5,9).isAtEndOfAChain(gameMap, playerOne));

        // Test5: top end of the chain for playerOne but not for playerTwo
        assertTrue(gameMap.at(8,6).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(8,6).isAtEndOfAChain(gameMap, playerTwo));

        // Test6: bottom end of the chain for playerOne but not for playerTwo
        assertTrue(gameMap.at(6,10).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(6,10).isAtEndOfAChain(gameMap, playerTwo));

        gameMap.at(7,7).removeSettlement();
        gameMap.at(7,8).removeSettlement();
        gameMap.at(6,9).removeSettlement();


        // left diagonal chain.
        gameMap.at(6,7).placeSettlement(playerOne);
        gameMap.at(7,8).placeSettlement(playerOne);
        gameMap.at(7,9).placeSettlement(playerOne);

        // Test7: Tile left and right is not a part of the chain
        assertFalse(gameMap.at(6,8).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(8,8).isAtEndOfAChain(gameMap, playerOne));

        // Test8: top end of the chain for playerOne but not for playerTwo
        assertTrue(gameMap.at(6,6).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(6,6).isAtEndOfAChain(gameMap, playerTwo));

        // Test9: bottom end of the chain for playerOne but not for playerTwo
        assertTrue(gameMap.at(8,10).isAtEndOfAChain(gameMap, playerOne));
        assertFalse(gameMap.at(8,10).isAtEndOfAChain(gameMap, playerTwo));

        gameMap.at(6,7).removeSettlement();
        gameMap.at(7,8).removeSettlement();
        gameMap.at(7,9).removeSettlement();
    }

    @Test
    void testSurroundingTilesIterator() {
        // TODO: test this.
    }

    @Test
    void testSurroundingTiles() {

        // Test1: surrounding tiles of tile (3,6)
        Set<Tile> surroundingTiles = gameMap.at(3, 6).surroundingTiles(gameMap).collect(Collectors.toSet());

        assertTrue(surroundingTiles.contains(gameMap.at(2,5)));
        assertTrue(surroundingTiles.contains(gameMap.at(3,5)));
        assertTrue(surroundingTiles.contains(gameMap.at(2,7)));
        assertTrue(surroundingTiles.contains(gameMap.at(3,7)));
        assertTrue(surroundingTiles.contains(gameMap.at(2,6)));
        assertTrue(surroundingTiles.contains(gameMap.at(4,6)));

        // Test2: surrounding tiles at the corner of tile (0,0)
        surroundingTiles = gameMap.at(0, 0).surroundingTiles(gameMap).collect(Collectors.toSet());

        assertTrue(surroundingTiles.contains(gameMap.at(0,1)));
        assertTrue(surroundingTiles.contains(gameMap.at(1,0)));

        // Test3: surrounding tiles at the side of tile (0,10)
        surroundingTiles = gameMap.at(0, 10).surroundingTiles(gameMap).collect(Collectors.toSet());

        assertTrue(surroundingTiles.contains(gameMap.at(0,9)));
        assertTrue(surroundingTiles.contains(gameMap.at(0,11)));
        assertTrue(surroundingTiles.contains(gameMap.at(1,10)));
    }

    @Test
    void testSurroundingTilesPaddock() {

        // Test1: surrounding tiles paddock with no obstacles and skipping obstacle.
        Set<Tile> paddock = gameMap.at(10,16).surroundingTilesPaddock(gameMap).collect(Collectors.toSet());
        assertTrue(paddock.contains(gameMap.at(9,14)));
        assertTrue(paddock.contains(gameMap.at(11,14)));
        assertTrue(paddock.contains(gameMap.at(8,16)));
        assertTrue(paddock.contains(gameMap.at(12,16)));
        assertTrue(paddock.contains(gameMap.at(9,18)));
        assertTrue(paddock.contains(gameMap.at(11,18)));

        // Test2: surrounding tiles paddock at the left border.
        paddock = gameMap.at(0,10).surroundingTilesPaddock(gameMap).collect(Collectors.toSet());
        assertTrue(paddock.contains(gameMap.at(1,8)));
        assertTrue(paddock.contains(gameMap.at(2,10)));
        assertTrue(paddock.contains(gameMap.at(1,12)));

        // Test3: surrounding tiles paddock in the bottom right corner.
        paddock = gameMap.at(19,19).surroundingTilesPaddock(gameMap).collect(Collectors.toSet());
        assertTrue(paddock.contains(gameMap.at(18,17)));
        assertTrue(paddock.contains(gameMap.at(17,19)));

        // test4: surrounding tiles paddock with obstacle token.
        paddock = gameMap.at(17,15).surroundingTilesPaddock(gameMap).collect(Collectors.toSet());
        assertFalse(paddock.contains(gameMap.at(18,12)));

        // test5: surrounding tiles paddock with obstacle water.
        paddock = gameMap.at(16,10).surroundingTilesPaddock(gameMap).collect(Collectors.toSet());
        assertFalse(paddock.contains(gameMap.at(15,8)));

        // test6: surrounding tiles paddock with obstacle mountain.
        paddock = gameMap.at(4,13).surroundingTilesPaddock(gameMap).collect(Collectors.toSet());
        assertFalse(paddock.contains(gameMap.at(5,11)));

    }

    @Test
    void testSurroundingSettlements() {

        // Test1: surrounding settlements at (4,13)
        gameMap.at(4,12).placeSettlement(playerOne);
        gameMap.at(5,12).placeSettlement(playerTwo); // different player
        gameMap.at(5,13).placeSettlement(playerOne);
        gameMap.at(4,14).placeSettlement(playerOne);

        Set<Tile> settlements = gameMap.at(4,13).surroundingSettlements(gameMap, playerOne).collect(Collectors.toSet());
        assertTrue(settlements.contains(gameMap.at(4,12)));
        assertFalse(settlements.contains(gameMap.at(5,12)));
        assertTrue(settlements.contains(gameMap.at(5,13)));
        assertTrue(settlements.contains(gameMap.at(4,14)));

        gameMap.at(4,12).removeSettlement();
        gameMap.at(5,12).removeSettlement();
        gameMap.at(5,13).removeSettlement();
        gameMap.at(4,14).removeSettlement();
    }

    @Test
    void testSurroundingTokenTiles() {

        // Test1: token at (7,16)
        Set<Tile> token = gameMap.at(7,15).surroundingTokenTiles(gameMap).collect(Collectors.toSet());
        assertTrue(token.contains(gameMap.at(7, 16)));

        // test2: token at (5,3)
        token = gameMap.at(6,3).surroundingTokenTiles(gameMap).collect(Collectors.toSet());
        assertTrue(token.contains(gameMap.at(5, 3)));
    }

    @Test
    void testHasSurroundingSettlement() {

        // Test1: surrounding settlements of playerOne and playerTwo at tile (4,13)
        gameMap.at(4,12).placeSettlement(playerOne);
        gameMap.at(5,12).placeSettlement(playerTwo); // different player
        gameMap.at(5,13).placeSettlement(playerOne);
        gameMap.at(4,14).placeSettlement(playerOne);

        assertTrue(gameMap.at(4,13).hasSurroundingSettlement(gameMap, playerOne));
        assertTrue(gameMap.at(4,13).hasSurroundingSettlement(gameMap, playerOne));
        assertFalse(gameMap.at(4,13).hasSurroundingSettlement(gameMap, playerThree));

        gameMap.at(4,12).removeSettlement();
        gameMap.at(5,12).removeSettlement();
        gameMap.at(5,13).removeSettlement();
        gameMap.at(4,14).removeSettlement();
    }

    @Test
    void testIsNextToSpecial() {
        Set<Tile> specials = new HashSet<>();
        specials.add(gameMap.at(2,7));

        gameMap.at(2,8).placeSettlement(playerOne);
        assertEquals(specials, gameMap.at(2,8).isNextToSpecial(gameMap));

        specials.clear();

        gameMap.at(2,9).placeSettlement(playerOne);
        assertEquals(specials, gameMap.at(2,9).isNextToSpecial(gameMap));
    }

    @Test
    void testPlaceSettlement() {
        // Test1: placed successfully
        gameMap.at(4,12).placeSettlement(playerOne);
        assertEquals(playerOne, gameMap.at(4,12).occupiedBy());
        gameMap.at(4,12).removeSettlement();

    }

    @Test
    void testRemoveSettlement() {

        // Test1: removed successfully
        gameMap.at(4,12).placeSettlement(playerOne);
        gameMap.at(4,12).removeSettlement();
        assertNull(gameMap.at(4,12).occupiedBy());
    }

    @Test
    void testMoveSettlement() {

        // Test1: placed successfully
        gameMap.at(4,12).placeSettlement(playerOne);
        assertEquals(playerOne, gameMap.at(4,12).occupiedBy());

        // Test2 moved successfully
        gameMap.at(4,12).moveSettlement(gameMap.at(0,15));
        assertEquals(playerOne, gameMap.at(0,15).occupiedBy());
        assertNull(gameMap.at(4,12).occupiedBy());

        gameMap.at(0,15).removeSettlement();
    }

    @Test
    void testTakeTokenFromSpecialPlace() {
        assertEquals(2, gameMap.at(2,7).remainingTokens);

        gameMap.at(2,7).takeTokenFromSpecialPlace();
        assertEquals(1, gameMap.at(2,7).remainingTokens);

        gameMap.at(2,7).takeTokenFromSpecialPlace();
        assertEquals(0, gameMap.at(2,7).remainingTokens);

        Exception exception =
                assertThrows(
                        HasNoTokenException.class,
                        () -> gameMap.at(2,7).takeTokenFromSpecialPlace());

        String expectedMessage = "No more tokens remaining!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        exception =
                assertThrows(
                        HasNoTokenException.class,
                        () -> gameMap.at(2,6).takeTokenFromSpecialPlace());

        expectedMessage = "Can't take a token from a non special place!";
        actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCalculateQuadrant() {
        assertEquals(Quadrants.TOP_LEFT, gameMap.at(2,2).calculateQuadrant(10));
        assertEquals(Quadrants.TOP_RIGHT, gameMap.at(0, 10).calculateQuadrant(10));
        assertEquals(Quadrants.BOTTOM_LEFT, gameMap.at(12,2).calculateQuadrant(10));
        assertEquals(Quadrants.BOTTOM_RIGHT, gameMap.at(12, 12).calculateQuadrant(10));
    }
}