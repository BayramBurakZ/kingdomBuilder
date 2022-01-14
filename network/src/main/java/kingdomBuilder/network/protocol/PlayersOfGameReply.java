package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains information about the players of the current game -
 * reply message ?playersofgame {@link kingdomBuilder.network.protocol.PlayersOfGameRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?playersofgame) <[#{gameId};{#{clientIds}}]>")
public record PlayersOfGameReply(
        int gameId,
        List<Integer> clientIds
) {}
