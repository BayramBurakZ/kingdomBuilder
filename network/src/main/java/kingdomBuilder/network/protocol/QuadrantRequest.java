package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests details of one quadrant.
 */
@Protocol(format = "?quadrant #{quadrantId}")
public record QuadrantRequest(
        int quadrantId
) {
}
