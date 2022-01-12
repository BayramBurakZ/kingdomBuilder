package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Requests message to authenticate the client at the server.
 */
@Protocol(format = "iam #{name}")
public record IAm(
    String name
) {
}
