package kingdomBuilder.gui.gameboard;

import javafx.scene.image.Image;
import kingdomBuilder.model.TileType;

/**
 * Class to load all relevant textures.
 */
public class TextureLoader {
    /**
     * Represents the Image for the Tile "Canyon".
     */
    private static final Image CANYON = new Image("kingdomBuilder/gui/textures/tiles/CANYON.png");
    /**
     * Represents the Image for the Tile "Castle".
     */
    private static final Image CASTLE = new Image("kingdomBuilder/gui/textures/tiles/CASTLE.png");
    /**
     * Represents the Image for the Tile "Desert".
     */
    private static final Image DESERT = new Image("kingdomBuilder/gui/textures/tiles/DESERT.png");
    /**
     * Represents the Image for the Tile "Flower".
     */
    private static final Image FLOWER = new Image("kingdomBuilder/gui/textures/tiles/FLOWER.png");
    /**
     * Represents the Image for the Tile "Forrest".
     */
    private static final Image FORREST = new Image("kingdomBuilder/gui/textures/tiles/FORREST.png");
    /**
     * Represents the Image for the Tile "Gras".
     */
    private static final Image GRAS = new Image("kingdomBuilder/gui/textures/tiles/GRAS.png");
    /**
     * Represents the Image for the Tile "Mountain".
     */
    private static final Image MOUNTAIN = new Image("kingdomBuilder/gui/textures/tiles/MOUNTAIN.png");
    /**
     * Represents the Image for the Tile "Water".
     */
    private static final Image WATER = new Image("kingdomBuilder/gui/textures/tiles/WATER.png");

    /**
     * Represents the Image for the Token "Barn".
     */
    private static final Image BARN = new Image("kingdomBuilder/gui/textures/tokens/BARN.png");
    /**
     * Represents the Image for the Token "Farm".
     */
    private static final Image FARM = new Image("kingdomBuilder/gui/textures/tokens/FARM.png");
    /**
     * Represents the Image for the Token "Harbor".
     */
    private static final Image HARBOR = new Image("kingdomBuilder/gui/textures/tokens/HARBOR.png");
    /**
     * Represents the Image for the Token "Oasis".
     */
    private static final Image OASIS = new Image("kingdomBuilder/gui/textures/tokens/OASIS.png");
    /**
     * Represents the Image for the Token "Oracle".
     */
    private static final Image ORACLE = new Image("kingdomBuilder/gui/textures/tokens/ORACLE.png");
    /**
     * Represents the Image for the Token "Paddock".
     */
    private static final Image PADDOCK = new Image("kingdomBuilder/gui/textures/tokens/PADDOCK.png");
    /**
     * Represents the Image for the Token "Tavern".
     */
    private static final Image TAVERN = new Image("kingdomBuilder/gui/textures/tokens/TAVERN.png");
    /**
     * Represents the Image for the Token "Tower".
     */
    private static final Image TOWER = new Image("kingdomBuilder/gui/textures/tokens/TOWER.png");

    /**
     * Constructs a new TextureLoader.
     */
    public TextureLoader() {
    }

    /**
     * Gets the Image for the given TileType.
     * @param type The Tile for requesting the Image.
     * @return The Image for the requested Tile.
     */
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

            // TODO: generates an exception:
            default:
                return null;
        }
    }
}
