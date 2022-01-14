package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests information of all games hosted on the server.
 */
@Protocol(format = "?games")
public record GamesRequest() {
}
