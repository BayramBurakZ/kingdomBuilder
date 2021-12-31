package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?mygame) <[#{clientId};#{gameType};#{gameId};#{gameName};#{gameDescription};" +
        "#{playerLimit};#{playersJoined};#{timeLimit};#{turnLimit};[#{quadrantId1};#{quadrantId2};#{quadrantId3};" +
        "#{quadrantId4}]]>")
public record MyGameReply(
        int clientId,
        String gameType,
        int gameId,
        String gameName,
        String gameDescription,
        int playerLimit,
        int playersJoined,
        int timeLimit,
        int turnLimit,
        int quadrantId1,
        int quadrantId2,
        int quadrantId3,
        int quadrantId4
) {
}
