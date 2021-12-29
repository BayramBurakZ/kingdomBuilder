package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "load #{namespace}")
public record Load(
        String namespace
) {}
