package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to use the tower token.
 */
@Protocol(format = "tower [#{row};#{column}]")
public record Tower(
        int row,
        int column
) {
}
