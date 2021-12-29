package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "tavern [#{row};#{column}]")
public record Tavern(
        int row,
        int column
) {
}
