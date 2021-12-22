package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [GAME_HOSTED] <[#{clientId};#{gameType};#{gameId};#{gameName};#{gameDescription};#{playerLimit};#{playerJoined}]>")
public record GameHosted(
    int clientId,
    String gameType,
    int gameId,
    String gameName,
    String gameDescription,
    int playerLimit,
    int playersJoined
) {}
