package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?playersofgame #{gameId}")
public record PlayersOfGameRequest(
        int gameId
) {
}
