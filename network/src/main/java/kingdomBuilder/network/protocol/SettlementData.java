package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[#{clientId};#{numberOfSettlements}]")
public record SettlementData(
        int clientId,
        int numberOfSettlements
) {
}
