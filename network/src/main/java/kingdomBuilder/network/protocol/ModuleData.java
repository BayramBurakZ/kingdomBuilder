package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents data of module.
 */
@Protocol(format = "[#{module};#{namespace}]", isComponent = true)
public record ModuleData(
        String module,
        String namespace
) {}
