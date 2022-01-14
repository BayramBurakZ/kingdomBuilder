package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests information on specific client.
 */
@Protocol(format = "?client #{clientId}")
public record ClientRequest(
        int clientId
) {
}
