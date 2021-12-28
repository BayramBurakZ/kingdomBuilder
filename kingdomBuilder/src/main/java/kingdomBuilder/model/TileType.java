package kingdomBuilder.model;

import java.util.HashMap;
import java.util.Map;
//TODO: this class should be part of DataLogic - so no javadoc needed right now

public enum TileType {
    CANYON(1),
    DESERT(2),
    FLOWER(3),
    FORREST(4),
    GRAS(5),
    MOUNTAIN(6),
    WATER(7),
    CASTLE(8),

    BARN(9),
    FARM(10),
    HARBOR(11),
    OASIS(12),
    ORACLE(13),
    PADDOCK(14),
    TAVERN(15),
    TOWER(16);

    private int value;
    private static Map map = new HashMap<>();

    private TileType(int value) {
        this.value = value;
    }

    static {
        for (TileType tileType : TileType.values()) {
            map.put(tileType.value, tileType);
        }
    }

    public static TileType valueOf(int tileType) {
        return (TileType) map.get(tileType);
    }

    public int getValue() {
        return value;
    }

}
