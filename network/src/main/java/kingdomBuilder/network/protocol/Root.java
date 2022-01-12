package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to become a root user.
 */
@Protocol(format = "root #{password}")
public record Root(
        String password
) {
}
