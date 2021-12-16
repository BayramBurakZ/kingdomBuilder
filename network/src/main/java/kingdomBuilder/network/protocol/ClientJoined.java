package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [CLIENT_JOINED] <[#{clientId};#{name};#{gameId}]>")
public record ClientJoined(
    int clientId,
    String name,
    int gameId
) { }
