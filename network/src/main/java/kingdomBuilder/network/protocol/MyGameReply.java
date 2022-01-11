package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?mygame) <[#{clientId};#{gameType};#{gameId};#{gameName};#{gameDescription};" +
        "#{playerLimit};#{playersJoined};#{timeLimit};#{turnLimit};#{boardData}]>")
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
        BoardData boardData
) {
}
