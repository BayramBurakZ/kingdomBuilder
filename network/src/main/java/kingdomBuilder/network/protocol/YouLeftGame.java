package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when receiver has left a game.
 */
@Protocol(format = "[SERVER_MESSAGE] [YOU_LEFT_GAME] <#{gameId}>")
public record YouLeftGame(
        int gameId
) {}
