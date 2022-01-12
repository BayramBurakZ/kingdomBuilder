package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains current turn number -
 * reply message ?turns {@link kingdomBuilder.network.protocol.TurnsRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?turns) <#{turns}>")
public record TurnsReply(
        int turns
) {
}
