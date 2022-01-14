package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains the player whose turn it is -
 * reply message ?settlementsleft {@link kingdomBuilder.network.protocol.WhoseTurnRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?whoseturn) <#{clientId}>")
public record WhoseTurnReply(
        int clientId
) {
}
