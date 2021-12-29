package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [YOUR_TERRAIN_CARD] <#{terrainType}>")
public record YourTerrainCard(
        String terrainType
) {
}
