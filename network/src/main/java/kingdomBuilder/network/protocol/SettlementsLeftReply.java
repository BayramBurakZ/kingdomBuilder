package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains the number of settlements left -
 * reply message ?settlementsleft {@link kingdomBuilder.network.protocol.SettlementsLeftRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?settlementsleft) <{#{settlementsDataList}}>")
public record SettlementsLeftReply(
        List<SettlementData> settlementsDataList
) {
}
