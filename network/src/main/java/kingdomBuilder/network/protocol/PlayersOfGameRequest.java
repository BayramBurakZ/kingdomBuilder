package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests information about the players of the current game.
 */
@Protocol(format = "?playersofgame #{gameId}")
public record PlayersOfGameRequest(
        int gameId
) {
}
