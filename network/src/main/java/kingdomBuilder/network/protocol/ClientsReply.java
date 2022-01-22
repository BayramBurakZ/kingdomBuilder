package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains information of all clients registered at the server -
 *  * reply message ?clients {@link ClientsRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?clients) <{#{clients}}>")
public record ClientsReply(
    List<ClientData> clients
) {
}
