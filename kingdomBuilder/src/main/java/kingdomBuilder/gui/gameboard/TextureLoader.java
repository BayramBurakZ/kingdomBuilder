package kingdomBuilder.gui.gameboard;


import javafx.scene.image.Image;
import kingdomBuilder.model.TileType;


public class TextureLoader {
    private final Image CANYON = new Image("kingdomBuilder/gui/textures/tiles/CANYON.png");
    private final Image CASTLE = new Image("kingdomBuilder/gui/textures/tiles/CASTLE.png");
    private final Image DESERT = new Image("kingdomBuilder/gui/textures/tiles/DESERT.png");
    private final Image FLOWER = new Image("kingdomBuilder/gui/textures/tiles/FLOWER.png");
    private final Image FORREST = new Image("kingdomBuilder/gui/textures/tiles/FORREST.png");
    private final Image GRAS = new Image("kingdomBuilder/gui/textures/tiles/GRAS.png");
    private final Image MOUNTAIN = new Image("kingdomBuilder/gui/textures/tiles/MOUNTAIN.png");
    private final Image WATER = new Image("kingdomBuilder/gui/textures/tiles/WATER.png");

    private final Image BARN = new Image("kingdomBuilder/gui/textures/tokens/BARN.png");
    private final Image FARM = new Image("kingdomBuilder/gui/textures/tokens/FARM.png");
    private final Image HARBOR = new Image("kingdomBuilder/gui/textures/tokens/HARBOR.png");
    private final Image OASIS = new Image("kingdomBuilder/gui/textures/tokens/OASIS.png");
    private final Image ORACLE = new Image("kingdomBuilder/gui/textures/tokens/ORACLE.png");
    private final Image PADDOCK = new Image("kingdomBuilder/gui/textures/tokens/PADDOCK.png");
    private final Image TAVERN = new Image("kingdomBuilder/gui/textures/tokens/TAVERN.png");
    private final Image TOWER = new Image("kingdomBuilder/gui/textures/tokens/TOWER.png");


    public TextureLoader() {
    }

    public Image getTexture(TileType type) {
        switch (type) {
            case CANYON:
                return CANYON;
            case DESERT:
                return DESERT;
            case FLOWER:
                return FLOWER;
            case FORREST:
                return FORREST;
            case GRAS:
                return GRAS;
            case MOUNTAIN:
                return MOUNTAIN;
            case WATER:
                return WATER;
            case CASTLE:
                return CASTLE;
            case BARN:
                return BARN;
            case FARM:
                return FARM;
            case HARBOR:
                return HARBOR;
            case OASIS:
                return OASIS;
            case ORACLE:
                return ORACLE;
            case PADDOCK:
                return PADDOCK;
            case TAVERN:
                return TAVERN;
            case TOWER:
                return TOWER;

            //generates an exception:
            default:
                return null;
        }
    }
}
