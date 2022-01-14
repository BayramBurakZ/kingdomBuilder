package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to use the tavern token.
 */
@Protocol(format = "tavern [#{row};#{column}]")
public record Tavern(
        int row,
        int column
) {
}
