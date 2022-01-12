package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the server version.
 */
@Protocol(format ="?version")
public record VersionRequest() {
}
