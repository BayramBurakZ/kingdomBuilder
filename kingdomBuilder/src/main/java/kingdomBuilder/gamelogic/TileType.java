package kingdomBuilder.gamelogic;

import java.util.EnumSet;

/**
 * Tiles that represent a hexagon in game board.
 */
public enum TileType {
    /**
     * Represents the grass type. It is a normal type.
     * Is part of following enum sets: regularTileTypes.
     */
    GRAS,
    /**
     * Represents the flower type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    FLOWER,
    /**
     * Represents the forest type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    FORREST,
    /**
     * Represents the canyon type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    CANYON,
    /**
     * Represents the desert type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    DESERT,
    /**
     * Represents the water type. It is a normal type.
     * Is part of following enum sets: regularTileTypes, placeableTileTypes.
     */
    WATER,
    /**
     * Represents the mountain type. It is a normal type.
     * Is part of following enum sets: regularTileTypes.
     */
    MOUNTAIN,
    /**
     * Represents the castle type. It is a normal type.
     * Is part of following enum sets: nonPlaceableTileTypes.
     */
    CASTLE,
    /**
     * Represents the oracle type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes.
     */
    ORACLE,
    /**
     * Represents the farm type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    FARM,
    /**
     * Represents the tavern type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    TAVERN,
    /**
     * Represents the tower type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    TOWER,
    /**
     * Represents the harbor type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    HARBOR,
    /**
     * Represents the paddock type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    PADDOCK,
    /**
     * Represents the barn type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    BARN,
    /**
     * Represents the oasis type. It is a token type.
     * Is part of following enum sets: nonPlaceableTileTypes, tokenType.
     */
    OASIS;
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
}
