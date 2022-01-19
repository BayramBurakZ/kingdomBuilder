package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to place a settlement in basic turn.
 */
@Protocol(format = "place [#{row};#{column}]")
public record Place(
        int row,
        int column
) {
}
