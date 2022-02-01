package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when a player used his/her last settlement.
 */
@Protocol(format = "[GAME_MESSAGE] [PLAYER_USED_LAST_SETTLEMENT] <#{clientId}>")
public record PlayerUsedLastSettlement(
        int clientId
) {
}
