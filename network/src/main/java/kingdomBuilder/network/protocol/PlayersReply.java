package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains information of the players of the current game -
 * reply message ?players {@link kingdomBuilder.network.protocol.PlayersRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?players) <{#{playerDataList}}>")
public record PlayersReply(
        List<PlayerData> playerDataList
) {
}
