package kingdomBuilder.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    String fifth = "GRAS;GRAS;GRAS;FLOWER;FLOWER;MOUNTAIN;CANYON;CANYON;WATER;WATER;GRAS;GRAS;FLOWER;FLOWER;FLOWER;" +
            "MOUNTAIN;CANYON;CANYON;WATER;WATER;GRAS;GRAS;FLOWER;CANYON;FLOWER;CANYON;CANYON;CASTLE;WATER;WATER;" +
            "FORREST;CASTLE;GRAS;GRAS;CANYON;GRAS;MOUNTAIN;DESERT;DESERT;WATER;FORREST;FORREST;FORREST;FORREST;" +
            "CANYON;GRAS;GRAS;MOUNTAIN;DESERT;DESERT;WATER;WATER;FORREST;WATER;WATER;WATER;FLOWER;DESERT;DESERT;" +
            "DESERT;GRAS;GRAS;WATER;FLOWER;FLOWER;WATER;FLOWER;FLOWER;DESERT;DESERT;FORREST;FORREST;GRAS;ORACLE;" +
            "FLOWER;WATER;FLOWER;FLOWER;MOUNTAIN;DESERT;FORREST;FORREST;GRAS;FORREST;WATER;DESERT;DESERT;CANYON;" +
            "CANYON;DESERT;FORREST;FORREST;FORREST;FORREST;WATER;DESERT;DESERT;CANYON;CANYON;CANYON";

    TileType[] quadrant1;
    TileType[] quadrant2;
    TileType[] quadrant3;
    TileType[] quadrant4;
    TileType[] quadrant5;

    GameMap gameMap;
    GameMap mapWithPlacements;

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
        quadrant5 = Arrays.stream(fifth.split(";")).map(TileType::valueOf).toArray(TileType[]::new);

        gameMap = new GameMap(2, quadrant1, quadrant2, quadrant3, quadrant4);

        //playerOne
        mapWithPlacements = new GameMap(2, quadrant1, quadrant2, quadrant3, quadrant4);
        mapWithPlacements.at(6,2).placeSettlement(playerOne);
        mapWithPlacements.at(7,2).placeSettlement(playerOne);
        mapWithPlacements.at(6,3).placeSettlement(playerOne);
        mapWithPlacements.at(5,4).placeSettlement(playerOne);
        mapWithPlacements.at(7,19).placeSettlement(playerOne);
        mapWithPlacements.at(8,19).placeSettlement(playerOne);
        mapWithPlacements.at(9,19).placeSettlement(playerOne);
        mapWithPlacements.at(10,19).placeSettlement(playerOne);
        mapWithPlacements.at(7,18).placeSettlement(playerOne);
        mapWithPlacements.at(11,18).placeSettlement(playerOne);


        //playerTwo
        mapWithPlacements.at(6,10).placeSettlement(playerTwo);
        mapWithPlacements.at(7,10).placeSettlement(playerTwo);
        mapWithPlacements.at(8,10).placeSettlement(playerTwo);
        mapWithPlacements.at(9,10).placeSettlement(playerTwo);
        mapWithPlacements.at(8,16).placeSettlement(playerTwo);
        mapWithPlacements.at(8,15).placeSettlement(playerTwo);
        mapWithPlacements.at(9,15).placeSettlement(playerTwo);
        mapWithPlacements.at(10,16).placeSettlement(playerTwo);
        mapWithPlacements.at(9,17).placeSettlement(playerTwo);
        mapWithPlacements.at(8,17).placeSettlement(playerTwo);


        //playerThree
        mapWithPlacements.at(12,8).placeSettlement(playerThree);
        mapWithPlacements.at(12,7).placeSettlement(playerThree);
        mapWithPlacements.at(15,16).placeSettlement(playerThree);
        mapWithPlacements.at(16,16).placeSettlement(playerThree);
        mapWithPlacements.at(17,16).placeSettlement(playerThree);
        mapWithPlacements.at(18,16).placeSettlement(playerThree);
        mapWithPlacements.at(18,15).placeSettlement(playerThree);
        mapWithPlacements.at(19,15).placeSettlement(playerThree);
        mapWithPlacements.at(19,14).placeSettlement(playerThree);
        mapWithPlacements.at(19,13).placeSettlement(playerThree);
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
    void testAllBasicTurnTiles() {
        Set<Tile> actualTiles;

        // Test1: samples
        playerOne.setTerrainCard(TileType.FORREST);
        playerOne.remainingSettlementsOfTurn = 3;
        actualTiles = Game.allBasicTurnTiles(mapWithPlacements, playerOne).collect(Collectors.toSet());

        assertTrue(mapWithPlacements.at(7,2).isBlocked());
        assertTrue(actualTiles.contains(mapWithPlacements.at(6,19)));
        assertTrue(actualTiles.contains(mapWithPlacements.at(6,18)));
        assertTrue(actualTiles.contains(mapWithPlacements.at(12,18)));

        // Test2: check if allBasicTurnTiles contains all tiles

        playerTwo.setTerrainCard(TileType.FORREST);
        actualTiles = Game.allBasicTurnTiles(gameMap, playerOne).collect(Collectors.toSet());

        Set<Tile> expectedTiles = new HashSet<>();

       gameMap.getTiles().forEach(tile -> {
           if (tile.tileType == TileType.FORREST)
               expectedTiles.add(tile);
       });

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenOracleTiles() {
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 0);
        playerFour.setTerrainCard(TileType.FORREST);

        Tile oracle = new Tile(19, 19, TileType.ORACLE, 2, 20);
        playerOne.addToken(oracle);
        playerOne.startTurn();

        playerFour.addToken(oracle);
        playerFour.startTurn();

        // no settlements remaining ---------------------
        assertTrue(Game.allTokenOracleTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // in middle of basic turn
        playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.setTerrainCard(TileType.FORREST);
        playerFour.decrementRemainingSettlements();

        assertTrue(Game.allTokenOracleTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function
        playerOne.setTerrainCard(TileType.DESERT);

        Set<Tile> actualTiles = Game.allTokenOracleTiles(gameMap, playerOne).collect(Collectors.toSet());
        Set<Tile> expectedTiles = new HashSet<>(
                gameMap.getAllPlaceableTiles(playerOne, TileType.DESERT).collect(Collectors.toSet()));

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenFarmTiles() {
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 0);
        playerFour.setTerrainCard(TileType.FORREST);

        Tile farm = new Tile(19, 19, TileType.FARM, 2, 20);
        playerOne.addToken(farm);
        playerOne.startTurn();

        playerFour.addToken(farm);
        playerFour.startTurn();

        // no settlements remaining ---------------------
        assertTrue(Game.allTokenFarmTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // in middle of basic turn
        playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.decrementRemainingSettlements();

        assertTrue(Game.allTokenFarmTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function
        Set<Tile> actualTiles = Game.allTokenFarmTiles(gameMap, playerOne).collect(Collectors.toSet());
        Set<Tile> expectedTiles = new HashSet<>(
                gameMap.getAllPlaceableTiles(playerOne, TileType.GRAS).collect(Collectors.toSet()));

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenOasisTiles() {
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 0);
        playerFour.setTerrainCard(TileType.FORREST);

        Tile oasis = new Tile(19, 19, TileType.OASIS, 2, 20);
        playerOne.addToken(oasis);
        playerOne.startTurn();

        playerFour.addToken(oasis);
        playerFour.startTurn();

        // no settlements remaining ---------------------
        assertTrue(Game.allTokenOasisTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // in middle of basic turn
        playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.decrementRemainingSettlements();

        assertTrue(Game.allTokenOasisTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function
        Set<Tile> actualTiles = Game.allTokenOasisTiles(gameMap, playerOne).collect(Collectors.toSet());
        Set<Tile> expectedTiles = new HashSet<>(
                gameMap.getAllPlaceableTiles(playerOne, TileType.DESERT).collect(Collectors.toSet()));

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenTavernTiles() {
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 0);
        playerFour.setTerrainCard(TileType.FORREST);

        Tile tavern = new Tile(19, 19, TileType.TAVERN, 2, 20);
        playerOne.addToken(tavern);
        playerOne.startTurn();

        playerFour.addToken(tavern);
        playerFour.startTurn();

        // no settlements remaining ---------------------
        assertTrue(Game.allTokenTavernTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // in middle of basic turn
        playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.decrementRemainingSettlements();

        assertTrue(Game.allTokenTavernTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function
        gameMap.at(5,10).placeSettlement(playerOne);
        gameMap.at(6,10).placeSettlement(playerOne);
        gameMap.at(7,10).placeSettlement(playerOne);

        Set<Tile> actualTiles = Game.allTokenTavernTiles(gameMap, playerOne).collect(Collectors.toSet());
        Set<Tile> expectedTiles = new HashSet<>();
        expectedTiles.add(gameMap.at(8,10));

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenTowerTiles() {
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 0);
        playerFour.setTerrainCard(TileType.FORREST);

        Tile tower = new Tile(19, 19, TileType.TOWER, 2, 20);
        playerOne.addToken(tower);
        playerOne.startTurn();

        playerFour.addToken(tower);
        playerFour.startTurn();

        // no settlements remaining ---------------------
        assertTrue(Game.allTokenTowerTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // in middle of basic turn
        playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.decrementRemainingSettlements();

        assertTrue(Game.allTokenTowerTiles(mapWithPlacements, playerFour).collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function

        Set<Tile> actualTiles = Game.allTokenTowerTiles(gameMap, playerOne).collect(Collectors.toSet());
        Set<Tile> expectedTiles = gameMap.getPlaceableTilesAtBorder(playerOne).collect(Collectors.toSet());

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));

        gameMap.at(19, 14).placeSettlement(playerOne);

        actualTiles = Game.allTokenTowerTiles(gameMap, playerOne).collect(Collectors.toSet());
        expectedTiles = gameMap.getPlaceableTilesAtBorder(playerOne).collect(Collectors.toSet());

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenBarnTiles() {
        Tile barn = new Tile(19, 19, TileType.BARN, 2, 20);
        playerOne.addToken(barn);
        playerOne.startTurn();

        //-----------------------------------------------
        // in middle of basic turn
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.decrementRemainingSettlements();
        playerOne.addToken(barn);

        assertTrue(Game.allTokenBarnTiles(mapWithPlacements, playerFour, false)
                .collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function
        // settlements
        gameMap.at(5,10).placeSettlement(playerOne);

        Set<Tile> actualTiles = Game.allTokenBarnTiles(mapWithPlacements, playerOne, false)
                .collect(Collectors.toSet());
        Set<Tile> expectedTiles = new HashSet<>(
                mapWithPlacements.getSettlements(playerOne).collect(Collectors.toSet())
        );

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));

        // test on preset map so it has more variety
        actualTiles = Game.allTokenBarnTiles(mapWithPlacements, playerOne, true)
                .collect(Collectors.toSet());
        expectedTiles = mapWithPlacements.getAllPlaceableTiles(playerOne, playerOne.getTerrainCard()).collect(Collectors.toSet());

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenHarborTiles() {
        Tile harbor = new Tile(19, 19, TileType.HARBOR, 2, 20);
        playerOne.addToken(harbor);
        playerOne.startTurn();

        //-----------------------------------------------
        // in middle of basic turn
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.decrementRemainingSettlements();
        playerOne.addToken(harbor);

        assertTrue(Game.allTokenHarborTiles(mapWithPlacements, playerFour, false)
                .collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function
        // settlements
        gameMap.at(5,10).placeSettlement(playerOne);

        Set<Tile> actualTiles = Game.allTokenHarborTiles(mapWithPlacements, playerOne, false)
                .collect(Collectors.toSet());
        Set<Tile> expectedTiles = new HashSet<>(
                mapWithPlacements.getSettlements(playerOne).collect(Collectors.toSet())
        );

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));

        // test on preset map so it has more variety
        actualTiles = Game.allTokenHarborTiles(gameMap, playerOne, true)
                .collect(Collectors.toSet());
        expectedTiles = gameMap.getTiles(TileType.WATER).collect(Collectors.toSet());

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
    }

    @Test
    void testAllTokenPaddockTiles() {
        Tile paddock = new Tile(19, 19, TileType.PADDOCK, 2, 20);
        playerOne.addToken(paddock);
        playerOne.startTurn();

        //-----------------------------------------------
        // in middle of basic turn
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 20);
        playerFour.decrementRemainingSettlements();
        playerOne.addToken(paddock);

        assertTrue(Game.allTokenPaddockTiles(mapWithPlacements, playerFour)
                .collect(Collectors.toSet()).isEmpty());

        //-----------------------------------------------
        // normal function
        // settlements
        gameMap.at(5,10).placeSettlement(playerOne);

        Set<Tile> actualTiles = Game.allTokenPaddockTiles(mapWithPlacements, playerOne)
                .collect(Collectors.toSet());
        Set<Tile> expectedTiles = new HashSet<>(
                mapWithPlacements.getSettlements(playerOne).collect(Collectors.toSet())
        );

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));

        // test on preset map so it has more variety
        actualTiles = Game.allTokenPaddockTiles(gameMap, playerOne, 5, 10)
                .collect(Collectors.toSet());
        expectedTiles.clear();
        expectedTiles.add(gameMap.at(6,8));
        expectedTiles.add(gameMap.at(7,10));
        expectedTiles.add(gameMap.at(6,12));
        expectedTiles.add(gameMap.at(4, 12));

        assertTrue(actualTiles.containsAll(expectedTiles));
        assertTrue(expectedTiles.containsAll(actualTiles));
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
        ArrayList<Player> players = new ArrayList<>();
        players.add(playerOne);
        players.add(playerTwo);
        players.add(playerThree);

        //Test#1: no settlement placed => all players have the highest amount of settlements
        assertEquals(48, Game.scoreLord(gameMap, playerOne, players));
        assertEquals(48, Game.scoreLord(gameMap, playerTwo, players));
        assertEquals(48, Game.scoreLord(gameMap, playerThree, players));

        //Test#2: PlayerOne has most settlements on upper left.
        gameMap.at(0,0).placeSettlement(playerOne);
        assertEquals(48, Game.scoreLord(gameMap, playerOne, players));
        assertEquals(42, Game.scoreLord(gameMap, playerTwo, players));
        assertEquals(42, Game.scoreLord(gameMap, playerThree, players));
        gameMap.at(0,0).removeSettlement();

        //Test#3: playerOne hast most settlements on upper left and bottom right.
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(19,19).placeSettlement(playerOne);
        assertEquals(48, Game.scoreLord(gameMap, playerOne, players));
        assertEquals(36, Game.scoreLord(gameMap, playerTwo, players));
        assertEquals(36, Game.scoreLord(gameMap, playerThree, players));
        gameMap.at(0,0).removeSettlement();
        gameMap.at(19,19).removeSettlement();

        //Test#4:
        // playerOne has second most settlements on upper left.
        // playerTwo has most settlements on upper left.
        gameMap.at(0,0).placeSettlement(playerTwo);
        gameMap.at(1,0).placeSettlement(playerTwo);
        gameMap.at(0,1).placeSettlement(playerOne);

        assertEquals(42, Game.scoreLord(gameMap, playerOne, players));
        assertEquals(48, Game.scoreLord(gameMap, playerTwo, players));
        assertEquals(36, Game.scoreLord(gameMap, playerThree, players));

        gameMap.at(0,0).removeSettlement();
        gameMap.at(1,0).removeSettlement();
        gameMap.at(0,1).removeSettlement();

        //Test#5: at quadrant border
        // playerOne has most on upper left.
        // playerTwo has most on upper right.
        // playerThree has second most on upper left and right.
        gameMap.at(9,0).placeSettlement(playerOne);
        gameMap.at(8,0).placeSettlement(playerOne);
        gameMap.at(19,0).placeSettlement(playerTwo);
        gameMap.at(19,1).placeSettlement(playerTwo);
        gameMap.at(9,1).placeSettlement(playerThree);
        gameMap.at(18,0).placeSettlement(playerThree);

        assertEquals(36, Game.scoreLord(gameMap, playerOne, players));
        assertEquals(36, Game.scoreLord(gameMap, playerTwo, players));
        assertEquals(36, Game.scoreLord(gameMap, playerThree, players));

        gameMap.at(9,0).removeSettlement();
        gameMap.at(8,0).removeSettlement();
        gameMap.at(19,0).removeSettlement();
        gameMap.at(19,1).removeSettlement();
        gameMap.at(9,1).removeSettlement();
        gameMap.at(18,0).removeSettlement();

        //Test#6:
        // playerOne has most on every quadrant.
        // playerTwo hast second most on every quadrant
        // playerThree does not get any points.
        gameMap.at(3,4).placeSettlement(playerOne);
        gameMap.at(3,5).placeSettlement(playerOne);
        gameMap.at(3,7).placeSettlement(playerOne);
        gameMap.at(15,6).placeSettlement(playerOne);
        gameMap.at(16,6).placeSettlement(playerOne);
        gameMap.at(16,7).placeSettlement(playerOne);
        gameMap.at(5,14).placeSettlement(playerOne);
        gameMap.at(5,15).placeSettlement(playerOne);
        gameMap.at(5,16).placeSettlement(playerOne);
        gameMap.at(17,16).placeSettlement(playerOne);
        gameMap.at(18,16).placeSettlement(playerOne);
        gameMap.at(19,16).placeSettlement(playerOne);

        gameMap.at(0,0).placeSettlement(playerTwo);
        gameMap.at(0,9).placeSettlement(playerTwo);
        gameMap.at(10,0).placeSettlement(playerTwo);
        gameMap.at(19,0).placeSettlement(playerTwo);
        gameMap.at(0,19).placeSettlement(playerTwo);
        gameMap.at(1,18).placeSettlement(playerTwo);
        gameMap.at(19,19).placeSettlement(playerTwo);
        gameMap.at(19,18).placeSettlement(playerTwo);

        gameMap.at(0,3).placeSettlement(playerThree);
        gameMap.at(19,6).placeSettlement(playerThree);
        gameMap.at(4,12).placeSettlement(playerThree);
        gameMap.at(14,15).placeSettlement(playerThree);

        assertEquals(48, Game.scoreLord(gameMap, playerOne, players));
        assertEquals(24, Game.scoreLord(gameMap, playerTwo, players));
        assertEquals(0, Game.scoreLord(gameMap, playerThree, players));

        gameMap.at(3,4).removeSettlement();
        gameMap.at(3,5).removeSettlement();
        gameMap.at(3,7).removeSettlement();
        gameMap.at(15,6).removeSettlement();
        gameMap.at(16,6).removeSettlement();
        gameMap.at(16,7).removeSettlement();
        gameMap.at(5,14).removeSettlement();
        gameMap.at(5,15).removeSettlement();
        gameMap.at(5,16).removeSettlement();
        gameMap.at(17,16).removeSettlement();
        gameMap.at(18,16).removeSettlement();
        gameMap.at(19,16).removeSettlement();

        gameMap.at(0,0).removeSettlement();
        gameMap.at(0,9).removeSettlement();
        gameMap.at(10,0).removeSettlement();
        gameMap.at(19,0).removeSettlement();
        gameMap.at(0,19).removeSettlement();
        gameMap.at(1,18).removeSettlement();
        gameMap.at(19,19).removeSettlement();
        gameMap.at(19,18).removeSettlement();

        gameMap.at(0,3).removeSettlement();
        gameMap.at(19,6).removeSettlement();
        gameMap.at(4,12).removeSettlement();
        gameMap.at(14,15).removeSettlement();
    }

    @Test
    void testScoreFarmer() {
        //Test#1: no settlement placed
        assertEquals(0, Game.scoreFarmer(gameMap, playerOne));

        // place in every quadrant 1 settlement
        gameMap.at(4,4).placeSettlement(playerOne);
        gameMap.at(14,14).placeSettlement(playerOne);
        gameMap.at(4,14).placeSettlement(playerOne);
        gameMap.at(14,4).placeSettlement(playerOne);

        gameMap.at(5,5).placeSettlement(playerTwo);
        gameMap.at(15,15).placeSettlement(playerTwo);
        gameMap.at(5,15).placeSettlement(playerTwo);
        gameMap.at(15,5).placeSettlement(playerTwo);

        //Test#2: add one settlement in upper left quadrant (largest group)
        gameMap.at(0,0).placeSettlement(playerOne);
        assertEquals(3, Game.scoreFarmer(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();

        //Test#3: settlements in different quadrants with different size
        // add one settlement upper left, add 2 settlement bottom right
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(19,19).placeSettlement(playerOne);
        gameMap.at(18,19).placeSettlement(playerOne);
        assertEquals(3, Game.scoreFarmer(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();
        gameMap.at(19,19).removeSettlement();
        gameMap.at(18,19).removeSettlement();

        //Test#4: same amount of settlements in different quadrants
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(19,19).placeSettlement(playerOne);
        assertEquals(3, Game.scoreFarmer(gameMap, playerOne));
        gameMap.at(0,0).removeSettlement();
        gameMap.at(19,19).removeSettlement();

        //Test#5: more than one as the least settlement count (not connected)
        gameMap.at(0,0).placeSettlement(playerOne);
        gameMap.at(19,19).placeSettlement(playerOne);
        gameMap.at(0,19).placeSettlement(playerOne);
        gameMap.at(19,0).placeSettlement(playerOne);
        assertEquals(6, Game.scoreFarmer(gameMap, playerOne));
        gameMap.at(19,0).removeSettlement();
        gameMap.at(0,19).removeSettlement();
        gameMap.at(19,19).removeSettlement();
        gameMap.at(0,0).removeSettlement();
    }

    @Test
    void testScoreMerchant() {
        //Test#1: no settlement placed
        assertEquals(0, Game.scoreMerchant(gameMap, playerOne));

        //Test#2: two connected places (2,16) and (7,16)
        gameMap.at(3, 16).placeSettlement(playerOne);
        gameMap.at(4, 16).placeSettlement(playerOne);
        gameMap.at(5, 16).placeSettlement(playerOne);
        gameMap.at(6, 16).placeSettlement(playerOne);
        assertEquals(8, Game.scoreMerchant(gameMap, playerOne));
        gameMap.at(3, 16).removeSettlement();
        gameMap.at(4, 16).removeSettlement();
        gameMap.at(5, 16).removeSettlement();
        gameMap.at(6, 16).removeSettlement();

        //Test#3: three connected Places (2,16) and (7,16) and (3,13)
        gameMap.at(3, 16).placeSettlement(playerOne);
        gameMap.at(4, 16).placeSettlement(playerOne);
        gameMap.at(5, 16).placeSettlement(playerOne);
        gameMap.at(6, 16).placeSettlement(playerOne);
        gameMap.at(3, 14).placeSettlement(playerOne);
        gameMap.at(3, 15).placeSettlement(playerOne);
        assertEquals(12, Game.scoreMerchant(gameMap, playerOne));
        gameMap.at(3, 16).removeSettlement();
        gameMap.at(4, 16).removeSettlement();
        gameMap.at(5, 16).removeSettlement();
        gameMap.at(6, 16).removeSettlement();
        gameMap.at(3, 14).removeSettlement();
        gameMap.at(3, 15).removeSettlement();

        //Test#4: three connected Places (2,16) and (7,16) and (11,16) + break a connection
        // special case because (7,17) is not placed, but it counts as connected
        gameMap.at(3, 16).placeSettlement(playerOne);
        gameMap.at(4, 16).placeSettlement(playerOne);
        gameMap.at(5, 16).placeSettlement(playerOne);
        gameMap.at(6, 16).placeSettlement(playerOne);
        gameMap.at(8, 16).placeSettlement(playerOne);
        gameMap.at(9, 16).placeSettlement(playerOne);
        gameMap.at(10, 16).placeSettlement(playerOne);
        assertEquals(12, Game.scoreMerchant(gameMap, playerOne));
        gameMap.at(9, 16).removeSettlement();
        // break an existing connection
        assertEquals(8, Game.scoreMerchant(gameMap, playerOne));
        gameMap.at(3, 16).removeSettlement();
        gameMap.at(4, 16).removeSettlement();
        gameMap.at(5, 16).removeSettlement();
        gameMap.at(6, 16).removeSettlement();
        gameMap.at(8, 16).removeSettlement();
        gameMap.at(10, 16).removeSettlement();
    }

    @Test
    void testScoreWorker(){

        //Test#1: add 6 settlements next to tavern token at (7,16).
        //total points = 6.
        gameMap.at(6, 15).placeSettlement(playerOne);
        gameMap.at(7, 15).placeSettlement(playerOne);
        gameMap.at(6, 16).placeSettlement(playerOne);
        gameMap.at(8, 16).placeSettlement(playerOne);
        gameMap.at(6, 17).placeSettlement(playerOne);
        gameMap.at(7, 17).placeSettlement(playerOne);

        assertEquals(6, Game.scoreWorker(gameMap, playerOne));
        assertEquals(TileType.TAVERN, gameMap.at(7,16).tileType);

        //Test#2: add 5 settlements next to oasis token at (15,7).
        //total points = 11.
        gameMap.at(14, 7).placeSettlement(playerOne);
        gameMap.at(15, 6).placeSettlement(playerOne);
        gameMap.at(16, 6).placeSettlement(playerOne);
        gameMap.at(16, 7).placeSettlement(playerOne);
        gameMap.at(16, 8).placeSettlement(playerOne);

        assertEquals(11, Game.scoreWorker(gameMap, playerOne));
        assertEquals(TileType.OASIS, gameMap.at(15,7).tileType);

        //Test#3: add 3 settlements next to castle at (13,3).
        //total points = 14.
        gameMap.at(3, 12).placeSettlement(playerOne);
        gameMap.at(3, 14).placeSettlement(playerOne);
        gameMap.at(4, 13).placeSettlement(playerOne);

        assertEquals(14, Game.scoreWorker(gameMap, playerOne));
        assertEquals(TileType.CASTLE, gameMap.at(3,13).tileType);

        //Test#4: remove a settlement at (8,16).
        //total points = 13.
        gameMap.at(8,16).removeSettlement();
        assertEquals(13, Game.scoreWorker(gameMap, playerOne));

        //Test#5: add settlements on the map that are not next to special places.
        //total points = 13.
        gameMap.at(0, 0).placeSettlement(playerOne);
        gameMap.at(9, 10).placeSettlement(playerOne);
        gameMap.at(7, 0).placeSettlement(playerOne);
        gameMap.at(2, 14).placeSettlement(playerOne);
        gameMap.at(9, 15).placeSettlement(playerOne);
        gameMap.at(17, 0).placeSettlement(playerOne);
        gameMap.at(15, 19).placeSettlement(playerOne);
        gameMap.at(16, 19).placeSettlement(playerOne);

        assertEquals(13, Game.scoreWorker(gameMap, playerOne));

    }

    @Test
    void testScoreFisher(){

        //Test#1: add 6 settlements next to water.
        //total points = 6.
        gameMap.at(9, 3).placeSettlement(playerOne);
        gameMap.at(9, 4).placeSettlement(playerOne);
        gameMap.at(9, 5).placeSettlement(playerOne);
        gameMap.at(9, 6).placeSettlement(playerOne);
        gameMap.at(9, 7).placeSettlement(playerOne);
        gameMap.at(9, 8).placeSettlement(playerOne);

        assertEquals(6, Game.scoreFisher(gameMap, playerOne));

        //Test#2: add 4 settlements next to water on each quadrant;
        //total points = 10.
        gameMap.at(15, 6).placeSettlement(playerOne);
        gameMap.at(14, 17).placeSettlement(playerOne);
        gameMap.at(4, 14).placeSettlement(playerOne);
        gameMap.at(3, 7).placeSettlement(playerOne);

        assertEquals(10, Game.scoreFisher(gameMap, playerOne));

        //Test#3: add 4 settlements on water.
        //total points = 10.
        gameMap.at(15, 8).placeSettlement(playerOne);
        gameMap.at(16, 8).placeSettlement(playerOne);
        gameMap.at(14, 19).placeSettlement(playerOne);
        gameMap.at(3, 14).placeSettlement(playerOne);

        assertEquals(10, Game.scoreFisher(gameMap, playerOne));

        //Test#3: add 4 settlements on random places not next to water.
        //total points = 10.
        gameMap.at(5, 12).placeSettlement(playerOne);
        gameMap.at(18, 18).placeSettlement(playerOne);
        gameMap.at(19, 18).placeSettlement(playerOne);
        gameMap.at(9, 15).placeSettlement(playerOne);

        assertEquals(10, Game.scoreFisher(gameMap, playerOne));
    }

    @Test
    void testScoreMiner(){

        //Test#1: add 8 settlements next to Mountain.
        //total points = 8.
        gameMap.at(6, 10).placeSettlement(playerOne);
        gameMap.at(7, 10).placeSettlement(playerOne);
        gameMap.at(7, 11).placeSettlement(playerOne);
        gameMap.at(8, 11).placeSettlement(playerOne);
        gameMap.at(9, 11).placeSettlement(playerOne);
        gameMap.at(10, 10).placeSettlement(playerOne);
        gameMap.at(11, 10).placeSettlement(playerOne);
        gameMap.at(9, 14).placeSettlement(playerOne);

        assertEquals(8, Game.scoreMiner(gameMap, playerOne));

        //Test#2: add 4 settlements next to Mountain on each quadrant;
        //total points = 12.
        gameMap.at(1, 0).placeSettlement(playerOne);
        gameMap.at(10, 2).placeSettlement(playerOne);
        gameMap.at(16, 12).placeSettlement(playerOne);
        gameMap.at(4, 11).placeSettlement(playerOne);

        assertEquals(12, Game.scoreMiner(gameMap, playerOne));

        //Test#3: add 4 settlements on water next to a mountain.
        //total points = 16.
        gameMap.at(15, 12).placeSettlement(playerOne);
        gameMap.at(14, 13).placeSettlement(playerOne);
        gameMap.at(14, 14).placeSettlement(playerOne);
        gameMap.at(3, 10).placeSettlement(playerOne);

        assertEquals(16, Game.scoreMiner(gameMap, playerOne));

        //Test#4: add 4 settlements on random places not next to mountain.
        //total points = 16.
        gameMap.at(5, 14).placeSettlement(playerOne);
        gameMap.at(15, 8).placeSettlement(playerOne);
        gameMap.at(10, 19).placeSettlement(playerOne);
        gameMap.at(9, 18).placeSettlement(playerOne);

        assertEquals(16, Game.scoreMiner(gameMap, playerOne));
    }

    @Test
    void testCanUseBasicTurn() {

        //out of bounds
        assertFalse(Game.canUseBasicTurn(mapWithPlacements, playerOne, 20, 20));
        assertFalse(Game.canUseBasicTurn(mapWithPlacements, playerOne, -1, -1));

        // set internal card to grass
        playerOne.setTerrainCard(TileType.GRAS);

        // free space adjacent
        assertTrue(Game.canUseBasicTurn(mapWithPlacements, playerOne, 5, 1));

        // free space NOT adjacent
        assertFalse(Game.canUseBasicTurn(mapWithPlacements, playerOne, 0, 0));

        // blocked space
        assertFalse(Game.canUseBasicTurn(mapWithPlacements, playerOne, 6, 2));

        // create new player with no settlements left
        Player playerFour = new Player(4, "TestPlayer4", PlayerColor.WHITE, 0);
        playerFour.setTerrainCard(TileType.FORREST);

        // no remaining Settlements
        assertFalse(Game.canUseBasicTurn(mapWithPlacements, playerFour, 0, 0));

        // wrong terrain card (Card: Grass, Tile: Forest)
        assertFalse(Game.canUseBasicTurn(mapWithPlacements, playerOne, 19, 6));

        // no settlement placed yet
        assertTrue(Game.canUseBasicTurn(gameMap, playerOne, 6, 0));
        assertTrue(Game.canUseBasicTurn(gameMap, playerOne, 9, 9));
        assertTrue(Game.canUseBasicTurn(gameMap, playerOne, 15, 4));

        // reset internal card
        playerOne.setTerrainCard(null);
    }

    @Test
    void testUnsafeCheckForTokens() {
        gameMap.at(3, 16).placeSettlement(playerOne);
        Game.unsafeCheckForTokens(gameMap, playerOne, 3, 16);

        assertSame(gameMap.at(2, 16).tileType, TileType.TAVERN);

        assertEquals(1, playerOne.getTokens().get(TileType.TAVERN).getTotal());

        // add another token
        gameMap.at(6, 16).placeSettlement(playerOne);
        Game.unsafeCheckForTokens(gameMap, playerOne, 6, 16);

        assertSame(gameMap.at(7, 16).tileType, TileType.TAVERN);

        assertEquals(2, playerOne.getTokens().get(TileType.TAVERN).getTotal());

        // add another type of token
        gameMap.at(3, 7).placeSettlement(playerOne);
        Game.unsafeCheckForTokens(gameMap, playerOne, 3, 7);

        assertSame(gameMap.at(2, 7).tileType, TileType.TOWER);

        assertEquals(1, playerOne.getTokens().get(TileType.TOWER).getTotal());
        assertEquals(2, playerOne.getTokens().get(TileType.TAVERN).getTotal());

        // place another settlement next to a special place where we already have a token
        // still 1 tower token
        gameMap.at(1, 7).placeSettlement(playerOne);
        Game.unsafeCheckForTokens(gameMap, playerOne, 3, 7);

        assertSame(gameMap.at(2, 7).tileType, TileType.TOWER);

        assertEquals(1, playerOne.getTokens().get(TileType.TOWER).getTotal());
        assertEquals(2, playerOne.getTokens().get(TileType.TAVERN).getTotal());

    }

    @Test
    void testUnsafeRemoveToken() {
        // get a token
        // same code as testUnsafeCheckForTokens
        gameMap.at(3, 16).placeSettlement(playerOne);
        assertSame(gameMap.at(2, 16).tileType, TileType.TAVERN);
        Game.unsafeCheckForTokens(gameMap, playerOne, 3, 16);

        assertEquals(1, playerOne.getTokens().get(TileType.TAVERN).getTotal());

        gameMap.at(3, 16).removeSettlement();
        Game.unsafeRemoveToken(gameMap, playerOne, 2, 16);
        assertEquals(0, playerOne.getTokens().get(TileType.TAVERN).getTotal());

    }

    @Test
    void testUseTokenOracle() {
        TileType t = TileType.ORACLE;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenOracle(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseTokenFarm() {
        TileType t = TileType.FARM;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenFarm(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseTokenTavern() {
        TileType t = TileType.TAVERN;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenTavern(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseTokenTower() {
        TileType t = TileType.TOWER;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenTower(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseTokenOasis() {
        TileType t = TileType.OASIS;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenOasis(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseTokenHarbor() {
        TileType t = TileType.HARBOR;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenHarbor(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseTokenPaddock() {
        TileType t = TileType.PADDOCK;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenPaddock(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseTokenBarn() {
        TileType t = TileType.BARN;
        Tile token = new Tile(19, 19, t, 2, 20);
        playerOne.addToken(token);
        playerOne.startTurn();

        Game.useTokenBarn(playerOne);

        assertSame(0, playerOne.getTokens().get(t).getRemaining());
    }

    @Test
    void testUseBasicTurn() {
        playerOne.setTerrainCard(TileType.FORREST);
        playerOne.startTurn();

        assertSame(TurnState.START_OF_TURN, playerOne.getCurrentTurnState());
        assertSame(3, playerOne.getRemainingSettlementsOfTurn());

        // first placement of basic turn
        Game.useBasicTurn(gameMap, playerOne, 0, 0);
        assertSame(TurnState.BASIC_TURN, playerOne.getCurrentTurnState());
        assertSame(2, playerOne.getRemainingSettlementsOfTurn());

        // second placement of basic turn
        Game.useBasicTurn(gameMap, playerOne, 0, 0);
        assertSame(1, playerOne.getRemainingSettlementsOfTurn());

        // third placement of basic turn
        Game.useBasicTurn(gameMap, playerOne, 0, 0);
        assertSame(0, playerOne.getRemainingSettlementsOfTurn());

        assertSame(TurnState.END_OF_TURN, playerOne.getCurrentTurnState());

    }
}