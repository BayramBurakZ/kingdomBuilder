package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [TERRAIN_TYPE_OF_TURN] <#{terrainType}>")
public record TerrainTypeOfTurn(
        String terrainType
) {
}
