package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests name and id by which this client is registered at the server.
 */
@Protocol(format = "?whoami")
public record WhoAmIRequest() {
}
