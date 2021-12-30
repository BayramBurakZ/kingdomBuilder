package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[#{clientId};#{color}]")
public record PlayerData(
        int clientId,
        String color
) {
}
