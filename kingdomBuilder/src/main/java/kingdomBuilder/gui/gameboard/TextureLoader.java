package kingdomBuilder.gui.gameboard;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import kingdomBuilder.gamelogic.Game;

/**
 * Class to load all relevant textures.
 */
public final class TextureLoader {

    //region BASIC TILES

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

    //endregion BASIC TILES

    //region TOKEN

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

    //endregion TOKEN

    //region WIN_ICONS

    /**
     * Represents the Icon for the WinCondition "ANCHORITE".
     */
    private static final Image ANCHORITE = new Image("kingdomBuilder/gui/textures/icons/winconditions/ANCHORITE.png");

    /**
     * Represents the Icon for the WinCondition "CITIZEN".
     */
    private static final Image CITIZEN = new Image("kingdomBuilder/gui/textures/icons/winconditions/CITIZEN.png");

    /**
     * Represents the Icon for the WinCondition "EXPLORER".
     */
    private static final Image EXPLORER = new Image("kingdomBuilder/gui/textures/icons/winconditions/EXPLORER.png");

    /**
     * Represents the Icon for the WinCondition "FARMER".
     */
    private static final Image FARMER = new Image("kingdomBuilder/gui/textures/icons/winconditions/FARMER.png");

    /**
     * Represents the Icon for the WinCondition "FISHER".
     */
    private static final Image FISHER = new Image("kingdomBuilder/gui/textures/icons/winconditions/FISHER.png");

    /**
     * Represents the Icon for the WinCondition "KNIGHT".
     */
    private static final Image KNIGHT = new Image("kingdomBuilder/gui/textures/icons/winconditions/KNIGHT.png");

    /**
     * Represents the Icon for the WinCondition "LORDS".
     */
    private static final Image LORD = new Image("kingdomBuilder/gui/textures/icons/winconditions/LORD.png");

    /**
     * Represents the Icon for the WinCondition "MERCHANT".
     */
    private static final Image MERCHANT = new Image("kingdomBuilder/gui/textures/icons/winconditions/MERCHANT.png");

    /**
     * Represents the Icon for the WinCondition "MINER".
     */
    private static final Image MINER = new Image("kingdomBuilder/gui/textures/icons/winconditions/MINER.png");

    /**
     * Represents the Icon for the WinCondition "WORKER".
     */
    private static final Image WORKER = new Image("kingdomBuilder/gui/textures/icons/winconditions/WORKER.png");
    //endregion WIN_ICONS

    /**
     * Constructor so this class never get instanced.
     */
    private TextureLoader() {
    }

    /**
     * Gets the Image for the given TileType.
     * @param type the Tile for requesting the Image.
     * @return The Image for the requested Tile.
     */
    public static Image getTileTexture(Game.TileType type) {
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

    /**
     * Gets the Image for the given WinCondition.
     * @param wincondition  The WinCondition for requesting the Image.
     * @return The Image for the requested WinCondition.
     */
    public static Image getWinConditionTexture(Game.WinCondition wincondition) {
        return switch (wincondition) {
            case ANCHORITE -> ANCHORITE;
            case CITIZEN -> CITIZEN;
            case EXPLORER -> EXPLORER;
            case FARMER -> FARMER;
            case FISHER -> FISHER;
            case KNIGHT -> KNIGHT;
            case LORDS -> LORD;
            case MERCHANT -> MERCHANT;
            case MINER -> MINER;
            case WORKER -> WORKER;

            // TODO: generates an exception:
            default -> null;
        };
    }

    /**
     * Creates an image with only one color.
     * @param red the red value.
     * @param green the green value.
     * @param blue the blue value.
     * @return Image with the given color.
     */
    public static Image generateImage(double red, double green, double blue) {
        WritableImage img = new WritableImage(1, 1);
        PixelWriter pixelWriter = img.getPixelWriter();

        Color color = Color.color(red, green, blue);
        pixelWriter.setColor(0, 0, color);
        return img ;
    }
}
