package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[#{clientId};#{gameType};#{gameId};#{gameName};#{gameDescription};#{playerLimit};#{playersJoined}]", isComponent = true)
public record GameData(
        int clientId,
        String gameType,
        int gameId,
        String gameName,
        String gameDescription,
        int playerLimit,
        int playersJoined
) {}
