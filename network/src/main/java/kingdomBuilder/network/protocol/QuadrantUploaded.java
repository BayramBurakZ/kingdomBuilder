package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when quadrant has been uploaded.
 */
@Protocol(format = "[SERVER_MESSAGE] [QUADRANT_UPLOADED] <[#{clientId};#{quadrantId}]>")
public record QuadrantUploaded(
        int clientId,
        int quadrantId
) {
}
