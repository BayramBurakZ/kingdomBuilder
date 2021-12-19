package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?clients) <{#{clients}}>")
public record RequestClientsResponse(
    List<ClientData> clients
) {
}
