package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains information of all games hosted on the server -
 * reply message ?games {@link kingdomBuilder.network.protocol.GamesRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?games) <{#{games}}>")
public record GamesReply(
        List<GameData> games
) {}
