package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message sends the terrain card of the current turn.
 */
@Protocol(format = "[GAME_MESSAGE] [TERRAIN_TYPE_OF_TURN] <#{terrainType}>")
public record TerrainTypeOfTurn(
        String terrainType
) {
}
