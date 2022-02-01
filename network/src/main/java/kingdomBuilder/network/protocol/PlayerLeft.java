package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when another player has left a game.
 */
@Protocol(format = "[SERVER_MESSAGE] [PLAYER_LEFT] <[#{clientId};#{gameId}]>")
public record PlayerLeft(
    int clientId,
    int gameId
) {}
