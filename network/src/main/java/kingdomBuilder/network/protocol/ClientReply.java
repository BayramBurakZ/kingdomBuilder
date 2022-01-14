package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains information regarding a specific client - reply message to
 * ?client-request {@link kingdomBuilder.network.protocol.ClientRequest}
 */
@Protocol(format = "[REPLY_MESSAGE] (?client) <[#{clientId};#{clientName};#{game id}]>")
public record ClientReply(
        int clientId,
        String clientName,
        int gameId
) {
}
