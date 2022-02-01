package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to use the oasis token.
 */
@Protocol(format = "oasis [#{row};#{column}]")
public record Oasis(
        int row,
        int column
) {
}
