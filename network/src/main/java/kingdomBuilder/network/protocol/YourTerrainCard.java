package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message contains the terrain card of your turn.
 */
@Protocol(format = "[GAME_MESSAGE] [YOUR_TERRAIN_CARD] <#{terrainType}>")
public record YourTerrainCard(
        String terrainType
) {
}
