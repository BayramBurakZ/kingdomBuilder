package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to use the oracle token.
 */
@Protocol(format = "oracle [#{row};#{column}]")
public record Oracle(
        int row,
        int column
) {
}
