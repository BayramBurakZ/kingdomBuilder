package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?quadrant #{quadrantId}")
public record QuadrantRequest(
        int quadrantId
) {
}
