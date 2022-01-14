package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents a message to use a barn token.
 */
@Protocol(format = "barn [#{rowFrom};#{columnFrom};#{row};#{column}]")
public record Barn(
        int rowFrom,
        int columnFrom,
        int row,
        int column
) {
}
