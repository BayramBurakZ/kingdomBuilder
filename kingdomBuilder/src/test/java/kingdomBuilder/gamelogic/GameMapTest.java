package kingdomBuilder.gamelogic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class GameMapTest {

    // quadrants that are stored in the server
    String first = "FORREST;FORREST;FLOWER;DESERT;DESERT;DESERT;DESERT;CANYON;DESERT;DESERT;" +
            "FORREST;MOUNTAIN;FLOWER;FLOWER;DESERT;CANYON;DESERT;CANYON;CANYON;CANYON;" +
            "FORREST;FORREST;FORREST;FLOWER;DESERT;DESERT;CANYON;TOWER;WATER;CANYON;" +
            "FORREST;FORREST;FLOWER;FLOWER;DESERT;DESERT;DESERT;DESERT;WATER;WATER;" +
            "MOUNTAIN;FLOWER;FLOWER;WATER;FLOWER;DESERT;DESERT;WATER;WATER;FORREST;" +
            "MOUNTAIN;GRAS;FLOWER;TOWER;WATER;WATER;WATER;FLOWER;FORREST;FORREST;" +
            "GRAS;MOUNTAIN;GRAS;GRAS;GRAS;WATER;FLOWER;FLOWER;FORREST;FORREST;" +
            "MOUNTAIN;MOUNTAIN;GRAS;WATER;WATER;CANYON;CASTLE;FLOWER;FLOWER;GRAS;" +
            "CANYON;MOUNTAIN;WATER;MOUNTAIN;CANYON;GRAS;GRAS;GRAS;GRAS;GRAS;" +
            "CANYON;CANYON;MOUNTAIN;MOUNTAIN;CANYON;CANYON;CANYON;GRAS;GRAS;GRAS";

    String second = "DESERT;DESERT;DESERT;WATER;WATER;WATER;WATER;WATER;WATER;WATER;DESERT;CANYON;DESERT;WATER;" +
            "WATER;FORREST;FORREST;CASTLE;WATER;WATER;CANYON;WATER;WATER;WATER;WATER;FORREST;CANYON;CANYON;CANYON;" +
            "WATER;WATER;FLOWER;FLOWER;FLOWER;WATER;WATER;FORREST;FLOWER;FLOWER;WATER;WATER;FLOWER;FLOWER;GRAS;" +
            "GRAS;GRAS;WATER;WATER;WATER;WATER;FORREST;FORREST;FORREST;FORREST;GRAS;GRAS;GRAS;OASIS;WATER;WATER;" +
            "FORREST;FORREST;FORREST;FLOWER;GRAS;CANYON;CANYON;DESERT;WATER;WATER;GRAS;FORREST;OASIS;FLOWER;GRAS;" +
            "CANYON;CANYON;DESERT;DESERT;WATER;GRAS;GRAS;FLOWER;FLOWER;FLOWER;DESERT;DESERT;CANYON;DESERT;WATER;GRAS;" +
            "GRAS;GRAS;FLOWER;FLOWER;CANYON;CANYON;WATER;WATER;WATER";

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


    // Converted quadrants in array
    TileType quadrant1[];
    TileType quadrant2[];
    TileType quadrant3[];
    TileType quadrant4[];

    GameMap gameMap;

    Player playerOne;
    Player playerTwo;
    Player playerThree;

    private final int QUADRANT_WIDTH = 10;

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
    public void testMapCreationFromQuadrants() {
        // Test if each tile is in the correct position.

        for (int y = 0; y < QUADRANT_WIDTH; y++) {
            for (int x = 0; x < QUADRANT_WIDTH; x++) {
                assertEquals(gameMap.at(x, y).tileType, quadrant1[x * QUADRANT_WIDTH + y]);
                assertEquals(x, gameMap.at(x, y).x);
                assertEquals(y, gameMap.at(x, y).y);
            }
        }
        for (int y = 0; y < QUADRANT_WIDTH; y++) {
            for (int x = 0; x < QUADRANT_WIDTH; x++) {
                assertEquals(gameMap.at(x + QUADRANT_WIDTH, y).tileType, quadrant2[x * QUADRANT_WIDTH + y]);
                assertEquals(x, gameMap.at(x, y).x);
                assertEquals(y, gameMap.at(x, y).y);
            }
        }
        for (int y = 0; y < QUADRANT_WIDTH; y++) {
            for (int x = 0; x < QUADRANT_WIDTH; x++) {
                assertEquals(gameMap.at(x, y + QUADRANT_WIDTH).tileType, quadrant3[x * QUADRANT_WIDTH + y]);
                assertEquals(x, gameMap.at(x, y).x);
                assertEquals(y, gameMap.at(x, y).y);
            }
        }
        for (int y = 0; y < QUADRANT_WIDTH; y++) {
            for (int x = 0; x < QUADRANT_WIDTH; x++) {
                assertEquals(
                        gameMap.at(x + QUADRANT_WIDTH, y + QUADRANT_WIDTH).tileType,
                        quadrant4[x * QUADRANT_WIDTH + y]
                );
                assertEquals(x, gameMap.at(x, y).x);
                assertEquals(y, gameMap.at(x, y).y);
            }
        }
    }

    @Test
    public void testGetEntireTerrainPlaceableTiles() {

        Tile gras = gameMap.at(5, 1);
        Tile flower = gameMap.at(1, 2);
        Tile desert = gameMap.at(0, 5);
        Tile forest = gameMap.at(0, 1);

        Iterator<Tile> allGrasTiles = gameMap.getTiles(TileType.GRAS).iterator();
        Iterator<Tile> allDesertTiles = gameMap.getTiles(TileType.DESERT).iterator();
        Iterator<Tile> allFlowerTiles = gameMap.getTiles(TileType.FLOWER).iterator();
        Iterator<Tile> allForestTiles = gameMap.getTiles(TileType.FORREST).iterator();

        Tile current;
        boolean foundTile = false;

        // check if types correct
        assertEquals(TileType.GRAS, gameMap.at(gras.x, gras.y).tileType, "Type is not gras");
        assertEquals(TileType.FLOWER, gameMap.at(flower.x, flower.y).tileType, "Type is not flower");
        assertEquals(TileType.DESERT, gameMap.at(desert.x, desert.y).tileType, "Type is not desert");
        assertEquals(TileType.FORREST, gameMap.at(forest.x, forest.y).tileType, "Type is not forest");

        // Gras
        while (allGrasTiles.hasNext()) {
            current = allGrasTiles.next();

            if (current.x == gras.x && current.y == gras.y)
                foundTile = true;
        }

        if (!foundTile)
            fail("gras tile failed");

        foundTile = false;

        // Flower
        while (allFlowerTiles.hasNext()) {
            current = allFlowerTiles.next();

            if (current.x == flower.x && current.y == flower.y)
                foundTile = true;
        }

        if (!foundTile)
            fail("flower tile failed");

        foundTile = false;


        // desert
        while (allDesertTiles.hasNext()) {
            current = allDesertTiles.next();

            if (current.x == desert.x && current.y == desert.y)
                foundTile = true;
        }

        if (!foundTile)
            fail("desert tile failed");

        foundTile = false;

        // forest
        while (allForestTiles.hasNext()) {
            current = allForestTiles.next();

            if (current.x == forest.x && current.y == forest.y) {
                foundTile = true;
                break;
            }
        }

        if (!foundTile)
            fail("forest tile failed");

        foundTile = false;

    }

    @Test
    public void testSpecialPlaceInSurroundingTileNextToSpecialPlace() {
        //TODO: clean up and finish it (maybe?)
        Tile tower = gameMap.at(5, 3);
        assertEquals(TileType.TOWER, gameMap.at(5, 3).tileType, "not a tower.");

        // Testing with tile that is on bottom left (after mirroring diagonally) of tower
        //Tile tokenToTest =  map.specialPlaceInSurrounding(5, 4);

        //assertEquals(TileType.TOWER, tokenToTest.tileType, "failed to find Token in surrounding.");
    }

    @Test
    @Disabled
    public void testUseTokenOraclePlayerUsingToken() {
        //TODO: clean up and finish it (maybe?)
        //need instance of a game to test this.

        Player player = new Player(0, "TestPlayer", PlayerColor.BLUE, 20);
        //player.startTurn(TileType.FORREST);
        //player.addToken(TileType.ORACLE);

        //check if player has received that token
        assertTrue(player.playerHasTokenLeft(TileType.ORACLE), "failed to add Oracle token");
        // assertEquals(1, player.getRemainingTokens(TileType.ORACLE), "Player doesn't have one Oracle token");
    }

    @Test
    void testGetAllPlaceableTilesNextToSettlements() {
        Player playerOne = new Player(0, "PlayerOne", PlayerColor.RED, 20);
        Set<Tile> result;

        //place a tile next to water and mountain and check if the result is the correct neighbouring tile
        gameMap.at(2, 9).placeSettlement(playerOne);
        result = gameMap.getAllPlaceableTilesNextToSettlements(playerOne, TileType.CANYON).collect(Collectors.toSet());

        assertEquals(1, result.size());
        assertTrue(result.contains(gameMap.at(1, 9)));
        result.clear();

        // no neighbouring tile with type forest
        result = gameMap.getAllPlaceableTilesNextToSettlements(playerOne, TileType.FORREST).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //reset placements
        gameMap.at(2, 9).removeSettlement();

        // multiple neighbouring tiles with type canyon
        gameMap.at(9, 15).placeSettlement(playerOne);
        result = gameMap.getAllPlaceableTilesNextToSettlements(playerOne, TileType.CANYON).collect(Collectors.toSet());
        assertEquals(5, result.size());
        result.clear();
    }

    @Test
    void testGetSettlements() {
        Player playerOne = new Player(0, "PlayerOne", PlayerColor.RED, 20);
        Player playerTwo = new Player(1, "PlayerTwo", PlayerColor.BLUE, 20);
        Set<Tile> result;

        //no settlement placed
        result = gameMap.getSettlements(playerOne).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //one settlement placed
        gameMap.at(4, 5).placeSettlement(playerOne);
        result = gameMap.getSettlements(playerOne).collect(Collectors.toSet());

        assertEquals(1, result.size());
        assertTrue(result.contains(gameMap.at(4, 5)));
        result.clear();

        //single settlements all over the map
        gameMap.at(19, 19).placeSettlement(playerOne);
        gameMap.at(19, 0).placeSettlement(playerOne);

        result = gameMap.getSettlements(playerOne).collect(Collectors.toSet());
        assertEquals(3, result.size());
        assertTrue(result.contains(gameMap.at(4, 5)));
        result.clear();

        //remove these settlements for next part
        gameMap.at(19, 19).removeSettlement();
        gameMap.at(19, 0).removeSettlement();

        //small group
        gameMap.at(3, 5).placeSettlement(playerOne);
        gameMap.at(2, 5).placeSettlement(playerOne);
        gameMap.at(3, 4).placeSettlement(playerOne);

        result = gameMap.getSettlements(playerOne).collect(Collectors.toSet());
        assertEquals(4, result.size());
        result.clear();

        //another player places one settlement (nothing should be changed)
        gameMap.at(19, 19).placeSettlement(playerTwo);

        result = gameMap.getSettlements(playerOne).collect(Collectors.toSet());
        assertEquals(4, result.size());
        result.clear();
    }

    @Test
    void testGetSettlementsOfQuadrant() {
        Set<Tile> result;

        //no settlement placed
        //top left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_LEFT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //top right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_RIGHT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //bottom left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_LEFT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //bottom right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_RIGHT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //one settlement placed
        gameMap.at(4, 5).placeSettlement(playerOne);

        //top left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_LEFT).collect(Collectors.toSet());

        assertEquals(1, result.size());
        assertTrue(result.contains(gameMap.at(4, 5)));
        result.clear();

        //top right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_RIGHT).collect(Collectors.toSet());

        assertEquals(0, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        result.clear();

        //bottom left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_LEFT).collect(Collectors.toSet());

        assertEquals(0, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        result.clear();

        //bottom right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_RIGHT).collect(Collectors.toSet());

        assertEquals(0, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        result.clear();

        //single settlements all over the map
        gameMap.at(19, 19).placeSettlement(playerOne); //bottom right
        gameMap.at(19, 0).placeSettlement(playerOne);//bottom left

        //top left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_LEFT).collect(Collectors.toSet());
        assertEquals(1, result.size());
        assertTrue(result.contains(gameMap.at(4, 5)));
        assertFalse(result.contains(gameMap.at(19, 19)));
        assertFalse(result.contains(gameMap.at(19, 0)));
        result.clear();

        //top right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_RIGHT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        assertFalse(result.contains(gameMap.at(19, 19)));
        assertFalse(result.contains(gameMap.at(19, 0)));
        result.clear();

        //bottom left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_LEFT).collect(Collectors.toSet());
        assertEquals(1, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        assertFalse(result.contains(gameMap.at(19, 19)));
        assertTrue(result.contains(gameMap.at(19, 0)));
        result.clear();

        //bottom right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_RIGHT).collect(Collectors.toSet());
        assertEquals(1, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        assertTrue(result.contains(gameMap.at(19, 19)));
        assertFalse(result.contains(gameMap.at(19, 0)));
        result.clear();

        //remove these settlements for next part
        gameMap.at(19, 19).removeSettlement();
        gameMap.at(19, 0).removeSettlement();

        //small group
        gameMap.at(3, 5).placeSettlement(playerOne);
        gameMap.at(2, 5).placeSettlement(playerOne);
        gameMap.at(3, 4).placeSettlement(playerOne);

        //top left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_LEFT).collect(Collectors.toSet());
        assertEquals(4, result.size());
        assertTrue(result.contains(gameMap.at(4, 5)));
        assertTrue(result.contains(gameMap.at(3, 5)));
        assertTrue(result.contains(gameMap.at(2, 5)));
        assertTrue(result.contains(gameMap.at(3, 4)));
        result.clear();

        //top right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_RIGHT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        assertFalse(result.contains(gameMap.at(3, 5)));
        assertFalse(result.contains(gameMap.at(2, 5)));
        assertFalse(result.contains(gameMap.at(3, 4)));
        result.clear();

        //bottom left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_LEFT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        assertFalse(result.contains(gameMap.at(3, 5)));
        assertFalse(result.contains(gameMap.at(2, 5)));
        assertFalse(result.contains(gameMap.at(3, 4)));
        result.clear();

        //bottom right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_RIGHT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        assertFalse(result.contains(gameMap.at(4, 5)));
        assertFalse(result.contains(gameMap.at(3, 5)));
        assertFalse(result.contains(gameMap.at(2, 5)));
        assertFalse(result.contains(gameMap.at(3, 4)));
        result.clear();

        //another player places one settlement (nothing should be changed)
        gameMap.at(19, 19).placeSettlement(playerTwo);

        //top left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_LEFT).collect(Collectors.toSet());
        assertEquals(4, result.size());
        result.clear();

        //top right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.TOP_RIGHT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //bottom left quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_LEFT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();

        //bottom right quadrant
        result = gameMap.getSettlementsOfQuadrant(playerOne, Quadrants.BOTTOM_RIGHT).collect(Collectors.toSet());
        assertEquals(0, result.size());
        result.clear();
    }

    @Test
    void testGetSettlementGroup() {
        Player playerOne = new Player(0, "PlayerOne", PlayerColor.RED, 20);
        Player playerTwo = new Player(1, "PlayerTwo", PlayerColor.BLUE, 20);
        Set<Tile> result = new HashSet<>();

        // group with one settlement
        gameMap.at(4, 5).placeSettlement(playerOne);
        gameMap.getSettlementGroup(result, playerOne, 4, 5);

        assertEquals(1, result.size());
        result.clear();

        // expand the group
        gameMap.at(3, 5).placeSettlement(playerOne);
        gameMap.at(2, 5).placeSettlement(playerOne);
        gameMap.at(3, 4).placeSettlement(playerOne);

        gameMap.getSettlementGroup(result, playerOne, 4, 5);

        assertEquals(4, result.size());
        result.clear();

        // different player settlement next to it
        gameMap.at(1, 5).placeSettlement(playerTwo);

        gameMap.getSettlementGroup(result, playerOne, 4, 5);

        assertEquals(4, result.size());
        result.clear();
    }

    @Test
    void getSettlementGroup() {
        Set<Tile> result;

        // group with one settlement
        gameMap.at(4, 5).placeSettlement(playerOne);
        result = gameMap.getSettlementGroup(playerOne, gameMap.at(4, 5));

        assertEquals(1, result.size());
        result.clear();

        // expand the group
        gameMap.at(3, 5).placeSettlement(playerOne);
        gameMap.at(2, 5).placeSettlement(playerOne);
        gameMap.at(3, 4).placeSettlement(playerOne);

        result = gameMap.getSettlementGroup(playerOne, gameMap.at(4, 5));

        assertEquals(4, result.size());
        result.clear();

        // different player settlement next to it
        gameMap.at(1, 5).placeSettlement(playerTwo);

        result = gameMap.getSettlementGroup(playerOne, gameMap.at(4, 5));

        assertEquals(4, result.size());
        result.clear();
    }

    @Test
    void testGetSurroundingGroups() {
        List<Set<Tile>> result;

        // group with one settlement
        gameMap.at(4, 5).placeSettlement(playerOne);
        result = gameMap.getSurroundingGroups(playerOne, gameMap.at(3, 5));

        assertEquals(1, result.size());
        result.clear();

        // expand the group
        gameMap.at(3, 5).placeSettlement(playerOne);
        gameMap.at(2, 5).placeSettlement(playerOne);
        gameMap.at(3, 4).placeSettlement(playerOne);

        result = gameMap.getSurroundingGroups(playerOne, gameMap.at(4, 4));

        assertEquals(1, result.size());
        result.clear();

        // different player settlement next to it
        gameMap.at(1, 5).placeSettlement(playerTwo);

        result = gameMap.getSurroundingGroups(playerOne, gameMap.at(5, 5));

        assertEquals(1, result.size());
        result.clear();
    }

    @Test
    void testTopLeftX() {
        int x = 7, y = 8;
        // odd distance
        int distance = 3;

        // test on even row
        int correctX = x;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topLeftX(correctX, y - i);
        assertEquals(correctX, GameMap.topLeftX(x, y, distance));

        // test on odd row
        correctX = x;
        y = 9;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topLeftX(correctX, y - i);
        assertEquals(correctX, GameMap.topLeftX(x, y, distance));

        // even distance
        distance = 4;

        // test on even row
        correctX = x;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topLeftX(correctX, y - i);
        assertEquals(correctX, GameMap.topLeftX(x, y, distance));

        // test on odd row
        correctX = x;
        y = 9;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topLeftX(correctX, y - i);
        assertEquals(correctX, GameMap.topLeftX(x, y, distance));
    }

    @Test
    void testTopRightX() {
        int x = 7, y = 8;
        // odd distance
        int distance = 3;

        // test on even row
        int correctX = x;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topRightX(correctX, y - i);
        assertEquals(correctX, GameMap.topRightX(x, y, distance));

        // test on odd row
        correctX = x;
        y = 9;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topRightX(correctX, y - i);
        assertEquals(correctX, GameMap.topRightX(x, y, distance));

        // even distance
        distance = 4;

        // test on even row
        correctX = x;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topRightX(correctX, y - i);
        assertEquals(correctX, GameMap.topRightX(x, y, distance));

        // test on odd row
        correctX = x;
        y = 9;
        for (int i = 0; i < distance; i++)
            correctX = GameMap.topRightX(correctX, y - i);
        assertEquals(correctX, GameMap.topRightX(x, y, distance));
    }

    @Test
    void testIsWithinBounds() {

        assertTrue(gameMap.isWithinBounds(0, 19));
        assertTrue(gameMap.isWithinBounds(5, 12));
        assertTrue(gameMap.isWithinBounds(1, 9));
        assertTrue(gameMap.isWithinBounds(0, 0));
        assertTrue(gameMap.isWithinBounds(19, 19));
        assertTrue(gameMap.isWithinBounds(1, 0));

        assertFalse(gameMap.isWithinBounds(20, 0));
        assertFalse(gameMap.isWithinBounds(20, 1));
        assertFalse(gameMap.isWithinBounds(10, -1));
        assertFalse(gameMap.isWithinBounds(-1, 5));
        assertFalse(gameMap.isWithinBounds(1, 20));

    }

    @Test
    void testGetTilesAtBorder() {

        Set<Tile> border = gameMap.getTilesAtBorder().collect(Collectors.toSet());

        assertTrue(border.contains(gameMap.at(0, 10)));
        assertTrue(border.contains(gameMap.at(10, 0)));
        assertTrue(border.contains(gameMap.at(0, 0)));
        assertTrue(border.contains(gameMap.at(19, 5)));
        assertTrue(border.contains(gameMap.at(4, 19)));
        assertTrue(border.contains(gameMap.at(0, 0)));

        assertFalse(border.contains(gameMap.at(1, 10)));
        assertFalse(border.contains(gameMap.at(2,8)));
        assertFalse(border.contains(gameMap.at(7,8)));
        assertFalse(border.contains(gameMap.at(2,2)));
        assertFalse(border.contains(gameMap.at(18,18)));
        assertFalse(border.contains(gameMap.at(9,9)));
    }

    @Test
    void testGetPlaceableTilesAtBorder() {

        Set<Tile> border = gameMap.getPlaceableTilesAtBorder(playerOne).collect(Collectors.toSet());

        // Test1: Check placeable tiles.
        assertTrue(border.contains(gameMap.at(0, 0)));
        assertTrue(border.contains(gameMap.at(19, 10)));
        assertTrue(border.contains(gameMap.at(9, 19)));

        // Test2: Check correct placeable tiles.
        gameMap.at(0,10).placeSettlement(playerOne);
        gameMap.at(11, 0).placeSettlement(playerOne);
        gameMap.at(19,18).placeSettlement(playerTwo);
        border = gameMap.getPlaceableTilesAtBorder(playerOne).collect(Collectors.toSet());

        assertTrue(border.contains(gameMap.at(0, 9)));
        assertTrue(border.contains(gameMap.at(0, 11)));

        assertTrue(border.contains(gameMap.at(10, 0)));
        assertTrue(border.contains(gameMap.at(12, 0)));

        //TODO: the following are failing
        assertFalse(border.contains(gameMap.at(0, 10)));
        assertFalse(border.contains(gameMap.at(19, 0)));
        assertFalse(border.contains(gameMap.at(19, 17)));
        assertFalse(border.contains(gameMap.at(19, 19)));
    }


    @Test
    void fewestSettlementsInAllQuadrants() {
        //one settlement placed
        gameMap.at(4, 5).placeSettlement(playerOne);
        assertEquals(0, gameMap.fewestSettlementsInAllQuadrants(playerOne));

        //single settlements in all quadrants
        gameMap.at(19, 19).placeSettlement(playerOne);
        gameMap.at(19, 0).placeSettlement(playerOne);
        gameMap.at(3, 10).placeSettlement(playerOne);

        assertEquals(1, gameMap.fewestSettlementsInAllQuadrants(playerOne));

        //small group
        gameMap.at(3, 5).placeSettlement(playerOne);
        gameMap.at(2, 5).placeSettlement(playerOne);
        gameMap.at(3, 4).placeSettlement(playerOne);

        assertEquals(1, gameMap.fewestSettlementsInAllQuadrants(playerOne));

        //another player places one settlement (nothing should be changed)
        gameMap.at(19, 18).placeSettlement(playerTwo);

        assertEquals(1, gameMap.fewestSettlementsInAllQuadrants(playerOne));
    }

    @Test
    void testRankOfSettlementsInQuadrant() {
        List<Player> players = Arrays.asList(playerOne, playerTwo, playerThree);

        //place settlements in top left quadrant
        gameMap.at(2,2).placeSettlement(playerOne);
        gameMap.at(2,3).placeSettlement(playerOne);
        gameMap.at(3,2).placeSettlement(playerOne);

        gameMap.at(5,5).placeSettlement(playerTwo);
        gameMap.at(5,6).placeSettlement(playerTwo);

        gameMap.at(7,7).placeSettlement(playerThree);

        //top left quadrant
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerOne, players, Quadrants.TOP_LEFT));
        assertEquals(1, gameMap.rankOfSettlementsInQuadrant(playerTwo, players, Quadrants.TOP_LEFT));
        assertEquals(0, gameMap.rankOfSettlementsInQuadrant(playerThree, players, Quadrants.TOP_LEFT));

        //top right quadrant
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerOne, players, Quadrants.TOP_RIGHT));
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerTwo, players, Quadrants.TOP_RIGHT));
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerThree, players, Quadrants.TOP_RIGHT));

        //bottom left quadrant
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerOne, players, Quadrants.BOTTOM_LEFT));
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerTwo, players, Quadrants.BOTTOM_LEFT));
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerThree, players, Quadrants.BOTTOM_LEFT));

        //bottom right quadrant
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerOne, players, Quadrants.BOTTOM_RIGHT));
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerTwo, players, Quadrants.BOTTOM_RIGHT));
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerThree, players, Quadrants.BOTTOM_RIGHT));

        //two players have the same count of settlements in same quadrant
        gameMap.at(5,7).placeSettlement(playerTwo);

        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerOne, players, Quadrants.TOP_LEFT));
        assertEquals(2, gameMap.rankOfSettlementsInQuadrant(playerTwo, players, Quadrants.TOP_LEFT));
        assertEquals(1, gameMap.rankOfSettlementsInQuadrant(playerThree, players, Quadrants.TOP_LEFT));
    }

    @Test
    void connectedSpecialPlaces() {
        //connecting 2 special places
        gameMap.at(5,4).placeSettlement(playerOne);
        gameMap.at(6,4).placeSettlement(playerOne);
        gameMap.at(7,4).placeSettlement(playerOne);
        gameMap.at(7,5).placeSettlement(playerOne);

        assertEquals(2, gameMap.connectedSpecialPlaces(playerOne));

        //connecting 3 special places
        gameMap.at(5,6).placeSettlement(playerOne);
        gameMap.at(6,6).placeSettlement(playerOne);
        gameMap.at(4,6).placeSettlement(playerOne);
        gameMap.at(3,6).placeSettlement(playerOne);

        assertEquals(3, gameMap.connectedSpecialPlaces(playerOne));

        //connecting 2 special places with settlements of playerOne
        //and connecting 3 special places with settlements mixed of playerOne and playerTwo
        gameMap.at(5,4).removeSettlement();
        gameMap.at(6,4).removeSettlement();

        gameMap.at(5,4).placeSettlement(playerTwo);
        gameMap.at(6,4).placeSettlement(playerTwo);

        assertEquals(2, gameMap.connectedSpecialPlaces(playerOne));

        //connecting 0 special places but surround special places
        gameMap.at(5,6).removeSettlement();
        gameMap.at(6,6).removeSettlement();
        gameMap.at(5,4).removeSettlement();
        gameMap.at(6,4).removeSettlement();

        assertEquals(0, gameMap.connectedSpecialPlaces(playerOne));
    }

    @Test
    void testGetSpecialPlacesOfGroup() {
        //connection between special place - special place
        Tile s1 = gameMap.at(17, 2);
        Tile s2 = gameMap.at(15, 7);
        assertSame(s1.tileType, TileType.OASIS);
        assertSame(s2.tileType, TileType.OASIS);

        gameMap.at(16,3).placeSettlement(playerOne);
        gameMap.at(16,4).placeSettlement(playerOne);
        gameMap.at(16,5).placeSettlement(playerOne);
        gameMap.at(16,6).placeSettlement(playerOne);

        Set<Tile> group = gameMap.getSettlementGroup(playerOne, gameMap.at(16,3));

        Set<Tile> specialPlaces = gameMap.getSpecialPlacesOfGroup(group);

        assertTrue(specialPlaces.contains(s1));
        assertTrue(specialPlaces.contains(s2));

        //connection between castle - special place

        Tile s3 = gameMap.at(18, 12);
        Tile s4 = gameMap.at(15, 17);
        assertSame(s3.tileType, TileType.PADDOCK);
        assertSame(s4.tileType, TileType.CASTLE);

        gameMap.at(16,16).placeSettlement(playerOne);
        gameMap.at(16,15).placeSettlement(playerOne);
        gameMap.at(17,14).placeSettlement(playerOne);
        gameMap.at(17,13).placeSettlement(playerOne);

        group = gameMap.getSettlementGroup(playerOne, gameMap.at(17,14));

        specialPlaces = gameMap.getSpecialPlacesOfGroup(group);

        assertTrue(specialPlaces.contains(s3));
        assertTrue(specialPlaces.contains(s4));

        //connection between castle - special place - special place

        Tile s5 = gameMap.at(11, 16);
        Tile s6 = gameMap.at(7, 16);
        Tile s7 = gameMap.at(3, 13);
        assertSame(s5.tileType, TileType.PADDOCK);
        assertSame(s6.tileType, TileType.TAVERN);
        assertSame(s7.tileType, TileType.CASTLE);

        gameMap.at(4,13).placeSettlement(playerOne);
        gameMap.at(5,14).placeSettlement(playerOne);
        gameMap.at(5,15).placeSettlement(playerOne);
        gameMap.at(6,15).placeSettlement(playerOne);
        gameMap.at(7,15).placeSettlement(playerOne);
        gameMap.at(8,15).placeSettlement(playerOne);
        gameMap.at(9,15).placeSettlement(playerOne);
        gameMap.at(10,15).placeSettlement(playerOne);

        group = gameMap.getSettlementGroup(playerOne, gameMap.at(4,13));

        specialPlaces = gameMap.getSpecialPlacesOfGroup(group);

        assertTrue(specialPlaces.contains(s5));
        assertTrue(specialPlaces.contains(s6));
        assertTrue(specialPlaces.contains(s7));

    }
}
