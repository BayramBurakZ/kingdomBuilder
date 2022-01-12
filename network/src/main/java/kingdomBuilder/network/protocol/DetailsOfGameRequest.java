package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests details of specific game.
 */
@Protocol(format = "?detailsofgame #{gameId}")
public record DetailsOfGameRequest(
        int gameId
) {
}
