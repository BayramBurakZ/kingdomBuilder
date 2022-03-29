package kingdomBuilder.gamelogic;

import kingdomBuilder.gui.SceneLoader;

import java.util.EnumSet;
import java.util.ResourceBundle;

/**
 * Tiles that represent a hexagon in game board.
 */
public enum TileType {
    /**
     * Represents the grass type. It is a normal type.
     * Is part of following enum sets: regularTileTypes.
     */
    GRAS {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("gras");
        }
    },
    /**
     * Represents the flower type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    FLOWER {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("flower");
        }
    },
    /**
     * Represents the forest type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    FORREST {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("forrest");
        }
    },
    /**
     * Represents the canyon type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    CANYON {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("canyon");
        }
    },
    /**
     * Represents the desert type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    DESERT {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("desert");
        }
    },
    /**
     * Represents the water type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    WATER {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("water");
        }
    },
    /**
     * Represents the mountain type. It is a normal type.
     * Is part of following enum sets: regularTileTypes.
     */
    MOUNTAIN {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("mountain");
        }
    },
    /**
     * Represents the castle type. It is a normal type.
     * Is part of following enum sets: nonPlaceableTileTypes.
     */
    CASTLE {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("castle");
        }
    },
    /**
     * Represents the oracle type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes.
     */
    ORACLE {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("oracle");
        }
    },
    /**
     * Represents the farm type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    FARM {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("farm");
        }
    },
    /**
     * Represents the tavern type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    TAVERN {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("tavern");
        }
    },
    /**
     * Represents the tower type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    TOWER {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("tower");
        }
    },
    /**
     * Represents the harbor type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    HARBOR {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("harbor");
        }
    },
    /**
     * Represents the paddock type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    PADDOCK {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("paddock");
        }
    },
    /**
     * Represents the barn type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    BARN {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("barn");
        }
    },
    /**
     * Represents the oasis type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    OASIS {
        @Override
        public String toStringLocalized() {
            return ResourceBundle.getBundle("kingdomBuilder/gui/gui",
                    SceneLoader.getLocale()).getString("oasis");
        }
    };
    /**
     * Types of token that can be acquired.
     */
    public static EnumSet<TileType> tokenType = EnumSet.range(ORACLE, OASIS);
    /**
     * Types of tiles where a settlement can never be placed.
     */
    public static EnumSet<TileType> nonPlaceableTileTypes = EnumSet.range(MOUNTAIN, OASIS);
    /**
     * Types of tiles where the settlement can be placed without a token.
     */
    public static EnumSet<TileType> placeableTileTypes = EnumSet.range(GRAS, DESERT);
    /**
     * Types of regular tiles.
     */
    public static EnumSet<TileType> regularTileTypes = EnumSet.range(GRAS, MOUNTAIN);
    /**
     * Types of special tiles.
     */
    public static EnumSet<TileType> specialPlacesTypes = EnumSet.range(CASTLE, OASIS);

    /**
     * Returns the name of this enum constant formatted and localized regarding the language setting in the game.
     *
     * @return the string in the localized form.
     */
    public abstract String toStringLocalized();
}
