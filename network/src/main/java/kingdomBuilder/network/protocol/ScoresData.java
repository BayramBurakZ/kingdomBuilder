package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents data of scores.
 */
@Protocol(format = "[#{clientId};#{score}]", isComponent = true)
public record ScoresData(
        int clientId,
        int score
) {
}
