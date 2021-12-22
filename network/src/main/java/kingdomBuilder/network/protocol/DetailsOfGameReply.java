package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[REPLY_MESSAGE] (?detailsofgame) <#{details}>")
public record DetailsOfGameReply(
        String details
) {}
