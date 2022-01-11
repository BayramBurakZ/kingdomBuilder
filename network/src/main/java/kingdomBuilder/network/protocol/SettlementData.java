package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[#{clientId};#{numberOfSettlements}]", isComponent = true)
public record SettlementData(
        int clientId,
        int numberOfSettlements
) {
}
