package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "oasis [#{row};#{column}]")
public record Oasis(
        int row,
        int column
) {
}
