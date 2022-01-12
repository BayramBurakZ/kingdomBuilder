package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to kick a client from the server.
 */
@Protocol(format = "kick client #{clientId}")
public record KickClient(
        int clientId
) {
}
