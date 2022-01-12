package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to use the farm token.
 */
@Protocol(format = "farm [#{row};#{column}]")
public record Farm(
        int row,
        int column
) {
}
