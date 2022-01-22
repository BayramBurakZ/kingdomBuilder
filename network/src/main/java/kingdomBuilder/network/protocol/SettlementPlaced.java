package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when a settlement has been placed.
 */
@Protocol(format = "[GAME_MESSAGE] [SETTLEMENT_PLACED] <[#{clientId};#{row};#{column}]>")
public record SettlementPlaced(
        int clientId,
        int row,
        int column
) {
}
