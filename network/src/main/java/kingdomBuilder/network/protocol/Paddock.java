package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "paddock [#{rowFrom};#{columnFrom};#{row};#{column}]")
public record Paddock(
        int rowFrom,
        int columnFrom,
        int row,
        int column
) {
}
