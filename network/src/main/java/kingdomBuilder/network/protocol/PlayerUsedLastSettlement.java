package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [PLAYER_USED_LAST_SETTLEMENT] <#{clientId}>")
public record PlayerUsedLastSettlement(
        int clientId
) {
}
