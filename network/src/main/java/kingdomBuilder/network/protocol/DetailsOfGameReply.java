package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that contains the details of the game - reply message to
 * ?detailsofgame-request {@link kingdomBuilder.network.protocol.DetailsOfGameRequest}
 */
@Protocol(format = "[REPLY_MESSAGE] (?detailsofgame) <#{details}>")
public record DetailsOfGameReply(
        String details
) {}
