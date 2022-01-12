package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains the name and id by which this client is registered at the server -
 *  *  * reply message ?whoami {@link kingdomBuilder.network.protocol.WhoAmIRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?whoami) <[#{clientId};#{clientName};#{gameId}]>")
public record WhoAmIReply(
        int clientId,
        String clientName,
        int gameId
) {}
