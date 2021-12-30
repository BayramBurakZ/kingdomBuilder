package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [QUADRANT_UPLOADED] <[#{clientId};#{quadrantId}]>")
public record QuadrantUploaded(
        int clientId,
        int quadrantId
) {
}
