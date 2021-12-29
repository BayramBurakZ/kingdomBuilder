package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?client #{clientId}")
public record ClientRequest(
        int clientId
) {
}
