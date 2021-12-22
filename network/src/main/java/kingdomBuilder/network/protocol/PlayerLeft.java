package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [PLAYER_LEFT] <[#{clientId};#{gameId}]>")
public record PlayerLeft(
    int clientId,
    int gameId
) {}
