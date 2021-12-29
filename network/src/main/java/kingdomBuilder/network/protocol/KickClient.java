package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "kick client #{clientId}")
public record KickClient(
        int clientId
) {
}
