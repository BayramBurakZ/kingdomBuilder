package kingdomBuilder.gui.gameboard;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import kingdomBuilder.model.TileType;

public final class MaterialLoader {

    private static final PhongMaterial CANYON = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/CANYON.png"), null, null, null);

    private static final PhongMaterial CASTLE = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/CASTLE.png"), null, null, null);

    private static final PhongMaterial DESERT = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/DESERT.png"), null, null, null);

    private static final PhongMaterial FLOWER = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/FLOWER.png"), null, null, null);

    private static final PhongMaterial FORREST = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/FORREST.png"), null, null, null);

    private static final PhongMaterial GRAS = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/GRAS.png"), null, null, null);

    private static final PhongMaterial MOUNTAIN = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/MOUNTAIN.png"), null, null, null);

    private static final PhongMaterial WATER = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/WATER.png"), null, null, null);

    private static final PhongMaterial BARN = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/BARN.png"), null, null, null);

    private static final PhongMaterial FARM = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/FARM.png"), null, null, null);

    private static final PhongMaterial HARBOR = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/HARBOR.png"), null, null, null);

    private static final PhongMaterial OASIS = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/OASIS.png"), null, null, null);

    private static final PhongMaterial ORACLE = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/ORACLE.png"), null, null, null);

    private static final PhongMaterial PADDOCK = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/PADDOCK.png"), null, null, null);

    private static final PhongMaterial TAVERN = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/TAVERN.png"), null, null, null);

    private static final PhongMaterial TOWER = new PhongMaterial
            (Color.WHITE, new Image("kingdomBuilder/gui/textures/uvMaps/TOWER.png"), null, null, null);

    public static final PhongMaterial RED = new PhongMaterial(Color.RED);

    private MaterialLoader() {

    }

    public static PhongMaterial getMaterial(TileType type) {
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
