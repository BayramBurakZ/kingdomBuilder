package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?detailsofgame #{gameId}")
public record DetailsOfGameRequest(
        int gameId
) {
}
