package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when receiver now spectates a game.
 */
@Protocol(format = "[SERVER_MESSAGE] [YOU_SPECTATE_GAME] <#{gameId}>")
public record YouSpectateGame(
        int gameId
) {}
