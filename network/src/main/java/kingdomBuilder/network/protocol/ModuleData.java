package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[#{module};#{namespace}]", isComponent = true)
public record ModuleData(
        String module,
        String namespace
) {}
