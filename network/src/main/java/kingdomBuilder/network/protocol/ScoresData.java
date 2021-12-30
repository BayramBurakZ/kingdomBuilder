package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[#{clientId};#{score}]", isComponent = true)
public record ScoresData(
        int clientId,
        int score
) {
}
