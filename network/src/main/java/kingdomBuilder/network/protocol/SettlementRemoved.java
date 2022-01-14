package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when a settlement has been removed.
 */
@Protocol(format = "[GAME_MESSAGE] [SETTLEMENT_REMOVED] <[#{clientId};#{row};#{column}]>")
public record SettlementRemoved(
        int clientId,
        int row,
        int column
) {
}
