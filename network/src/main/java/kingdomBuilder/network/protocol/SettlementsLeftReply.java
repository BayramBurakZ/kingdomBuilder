package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?settlementsleft) <{#{settlementsDataList}}>")
public record SettlementsLeftReply(
        List<SettlementData> settlementsDataList
) {
}
