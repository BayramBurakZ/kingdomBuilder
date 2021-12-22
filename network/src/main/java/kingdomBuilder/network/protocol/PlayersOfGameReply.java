package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?playersofgame) <[#{gameId};{#{clientIds}}]>")
public record PlayersOfGameReply(
        int gameId,
        List<Integer> clientIds
) {}
