package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when receiver stops spectate a game.
 */
@Protocol(format = "[SERVER_MESSAGE] [STOPPED_SPECTATING] <#{gameId}>")
public record StoppedSpectating(
        int gameId
) {}
