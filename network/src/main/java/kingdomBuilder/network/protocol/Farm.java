package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "farm [#{row};#{column}]")
public record Farm(
        int row,
        int column
) {
}
