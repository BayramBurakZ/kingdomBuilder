package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when a new game has been hosted.
 */
@Protocol(format = "[SERVER_MESSAGE] [GAME_HOSTED] <#{gameData}]>")
public record GameHosted(
    GameData gameData
) {}
