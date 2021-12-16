package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[SERVER_MESSAGE] [MESSAGE] <[#{clientId};#{receiverIds};#{message}]>")
public record Message(
    int clientId,
    List<Integer> receiverIds,
    String message
) {}
