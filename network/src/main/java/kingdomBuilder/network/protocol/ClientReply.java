package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?client) <[#{clientId};#{clientName};#{game id}]>")
public record ClientReply(
        int clientId,
        String clientName,
        int gameId
) {
}
