package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains all information of the current game for the requesting client -
 * reply message ?mygame {@link kingdomBuilder.network.protocol.MyGameRequest}.
 */
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
