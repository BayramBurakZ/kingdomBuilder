package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "oracle [#{row};#{column}]")
public record Oracle(
        int row,
        int column
) {
}
