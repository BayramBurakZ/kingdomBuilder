package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "join #{gameId}")
public record Join(
        int gameId
) {
}
