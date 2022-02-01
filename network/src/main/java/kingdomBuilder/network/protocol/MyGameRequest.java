package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests all information of the current game for the requesting client.
 */
@Protocol(format = "?mygame")
public record MyGameRequest() {
}
