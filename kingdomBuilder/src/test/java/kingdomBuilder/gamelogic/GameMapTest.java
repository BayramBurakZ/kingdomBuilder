package kingdomBuilder.gamelogic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

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

    private final int QUADRANT_WIDTH = 10;

    @BeforeEach
    public void testInitializeMap() {
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
        while( allGrasTiles.hasNext()){
            current = allGrasTiles.next();

            if( current.x == gras.x && current.y == gras.y)
                foundTile = true;
        }

        if(!foundTile)
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
    public void testSpecialPlaceInSurroundingTileNextToSpecialPlace(){

        Tile tower = gameMap.at(5, 3);
        assertEquals(TileType.TOWER, gameMap.at(5, 3).tileType, "not a tower.");

        // Testing with tile that is on bottom left (after mirroring diagonally) of tower
        //Tile tokenToTest =  map.specialPlaceInSurrounding(5, 4);

        //assertEquals(TileType.TOWER, tokenToTest.tileType, "failed to find Token in surrounding.");
    }

    @Test
    @Disabled
    public void testUseTokenOraclePlayerUsingToken(){
        // need instance of a game to test this.

        Player player = new Player(0, "TestPlayer", PlayerColor.BLUE, 20 );
        //player.startTurn(TileType.FORREST);
        //player.addToken(TileType.ORACLE);

        //check if player has received that token
        assertTrue(player.playerHasTokenLeft(TileType.ORACLE), "failed to add Oracle token");
       // assertEquals(1, player.getRemainingTokens(TileType.ORACLE), "Player doesn't have one Oracle token");
    }
}