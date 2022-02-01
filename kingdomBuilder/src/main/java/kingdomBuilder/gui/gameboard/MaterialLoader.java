package kingdomBuilder.gui.gameboard;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import kingdomBuilder.gamelogic.Game;

/**
 * Class that contains all materials for the 3D objects.
 */
public final class MaterialLoader {

    /**
     * The material for the canyon.
     */
    private static final PhongMaterial CANYON = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/CANYON.png"), null, null, null);

    /**
     * The material for the castle.
     */
    private static final PhongMaterial CASTLE = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/CASTLE.png"), null, null, null);

    /**
     * The material for the desert.
     */
    private static final PhongMaterial DESERT = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/DESERT.png"), null, null, null);

    /**
     * The material for the flower.
     */
    private static final PhongMaterial FLOWER = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/FLOWER.png"), null, null, null);

    /**
     * The material for the forest.
     */
    private static final PhongMaterial FORREST = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/FORREST.png"), null, null, null);

    /**
     * The material for the gras.
     */
    private static final PhongMaterial GRAS = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/GRAS.png"), null, null, null);

    /**
     * The material for the mountain.
     */
    private static final PhongMaterial MOUNTAIN = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/MOUNTAIN.png"), null, null, null);

    /**
     * The material for the water.
     */
    private static final PhongMaterial WATER = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/WATER.png"), null, null, null);

    /**
     * The material for the barn.
     */
    private static final PhongMaterial BARN = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/BARN.png"), null, null, null);

    /**
     * The material for the farm.
     */
    private static final PhongMaterial FARM = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/FARM.png"), null, null, null);

    /**
     * The material for the harbor.
     */
    private static final PhongMaterial HARBOR = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/HARBOR.png"), null, null, null);

    /**
     * The material for the oasis.
     */
    private static final PhongMaterial OASIS = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/OASIS.png"), null, null, null);

    /**
     * The material for the oracle.
     */
    private static final PhongMaterial ORACLE = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/ORACLE.png"), null, null, null);

    /**
     * The material for the paddock.
     */
    private static final PhongMaterial PADDOCK = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/PADDOCK.png"), null, null, null);

    /**
     * The material for the tavern.
     */
    private static final PhongMaterial TAVERN = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/TAVERN.png"), null, null, null);

    /**
     * The material for the tower.
     */
    private static final PhongMaterial TOWER = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/TOWER.png"), null, null, null);

    /**
     * The material for the red highlight.
     */
    public static final PhongMaterial RED = new PhongMaterial(Color.RED);

    /**
     * The material for the white highlight.
     */
    public static final PhongMaterial WHITE = new PhongMaterial(Color.WHITE);

    /**
     * The material for the midnightblue highlight.
     */
    public static final PhongMaterial MIDNIGHTBLUE = new PhongMaterial(Color.MIDNIGHTBLUE);

    /**
     * Constructor so this class never get instanced.
     */
    private MaterialLoader() {

    }

    /**
     * Gets the Material according to the given type.
     * @param type the requested type.
     * @return The material.
     */
    public static PhongMaterial getMaterial(Game.TileType type) {
        return switch (type) {
            case CANYON -> CANYON;
            case DESERT -> DESERT;
            case FLOWER -> FLOWER;
            case FORREST -> FORREST;
            case GRAS -> GRAS;
            case MOUNTAIN -> MOUNTAIN;
            case WATER -> WATER;
            case CASTLE -> CASTLE;
            case BARN -> BARN;
            case FARM -> FARM;
            case HARBOR -> HARBOR;
            case OASIS -> OASIS;
            case ORACLE -> ORACLE;
            case PADDOCK -> PADDOCK;
            case TAVERN -> TAVERN;
            case TOWER -> TOWER;

            // TODO: generates an exception:
            default -> null;
        };
    }
}
