package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message to load a module to make the commands of the respective namespace available.
 */
@Protocol(format = "load #{namespace}")
public record Load(
        String namespace
) {}
