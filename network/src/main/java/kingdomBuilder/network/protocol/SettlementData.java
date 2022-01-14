package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents the data of a settlement.
 */
@Protocol(format = "[#{clientId};#{numberOfSettlements}]", isComponent = true)
public record SettlementData(
        int clientId,
        int numberOfSettlements
) {
}
