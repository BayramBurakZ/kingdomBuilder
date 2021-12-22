package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [PLAYER_JOINED] <[#{clientId};#{gameId}]>")
public record PlayerJoined(
        int clientId,
        int gameId
) {}
