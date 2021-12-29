package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "barn [#{rowFrom};#{columnFrom};#{row};#{column}]")
public record Barn(
        int rowFrom,
        int columnFrom,
        int row,
        int column
) {
}
