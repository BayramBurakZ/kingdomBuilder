package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "spectate #{gameId}")
public record Spectate(
        int gameId
) {
}
