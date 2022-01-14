package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to unload namespace.
 */
@Protocol(format = "unload #{namespace}")
public record Unload(
        String namespace
) {
}
