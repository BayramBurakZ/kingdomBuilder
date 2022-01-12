package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to use the harbor token.
 */
@Protocol(format = "harbor [#{rowFrom};#{columnFrom};#{row};#{column}]")
public record Harbor(
        int rowFrom,
        int columnFrom,
        int row,
        int column
) {
}
