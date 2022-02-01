package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to host a new KingdomBuilder game.
 */
@Protocol(format = "host game [#{gameName};#{gameDescription};#{playerLimit};#{timeLimit};#{turnLimit};" +
        "[#{quadrantId1};#{quadrantId2};#{quadrantId3};#{quadrantId4}]]")
public record HostGame(
        String gameName,
        String gameDescription,
        int playerLimit,
        int timeLimit,
        int turnLimit,
        int quadrantId1,
        int quadrantId2,
        int quadrantId3,
        int quadrantId4
) {
}
