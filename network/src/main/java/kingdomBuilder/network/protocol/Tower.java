package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "tower [#{row};#{column}]")
public record Tower(
        int row,
        int column
) {
}
