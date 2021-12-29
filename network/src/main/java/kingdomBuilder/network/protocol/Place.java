package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "place [#{row};#{column}]")
public record Place(
        int row,
        int column
) {
}
