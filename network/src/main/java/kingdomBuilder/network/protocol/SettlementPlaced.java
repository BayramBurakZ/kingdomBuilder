package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[GAME_MESSAGE] [SETTLEMENT_PLACED] <[#{clientId};#{row};#{column}]>")
public record SettlementPlaced(
        int clientId,
        int row,
        int column
) {
}
