package kingdomBuilder.gui.gameboard;


import javafx.scene.image.Image;
import kingdomBuilder.model.TileType;


public class TextureLoader {
    private final Image CANYON = new Image("kingdomBuilder/gui/textures/CANYON.png");
    private final Image DESERT = new Image("kingdomBuilder/gui/textures/DESERT.png");
    private final Image FLOWER = new Image("kingdomBuilder/gui/textures/FLOWER.png");
    private final Image FORREST = new Image("kingdomBuilder/gui/textures/FORREST.png");
    private final Image GRAS = new Image("kingdomBuilder/gui/textures/GRAS.png");
    private final Image MOUNTAIN = new Image("kingdomBuilder/gui/textures/MOUNTAIN.png");
    private final Image WATER = new Image("kingdomBuilder/gui/textures/WATER.png");

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
            //generates an exception:
            default:
                return null;
        }
    }
}
