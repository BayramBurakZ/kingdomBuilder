package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [STOPPED_SPECTATING] <#{gameId}>")
public record StoppedSpectating(
        int gameId
) {}
