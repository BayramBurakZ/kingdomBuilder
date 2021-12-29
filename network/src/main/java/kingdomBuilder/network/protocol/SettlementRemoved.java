package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [SETTLEMENT_REMOVED] <[#{clientId};#{row};#{column}]>")
public record SettlementRemoved(
        int clientId,
        int row,
        int column
) {
}
