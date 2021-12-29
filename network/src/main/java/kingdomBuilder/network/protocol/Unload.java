package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "unload #{namespace}")
public record Unload(
        String namespace
) {
}
