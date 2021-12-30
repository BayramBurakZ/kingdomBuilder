package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?players) <{#{playerDataList}}>")
public record PlayersReply(
        List<PlayerData> playerDataList
) {
}
