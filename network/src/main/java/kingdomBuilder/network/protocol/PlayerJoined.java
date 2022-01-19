package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when another player has been joined a game.
 */
@Protocol(format = "[SERVER_MESSAGE] [PLAYER_JOINED] <[#{clientId};#{gameId}]>")
public record PlayerJoined(
        int clientId,
        int gameId
) {}
