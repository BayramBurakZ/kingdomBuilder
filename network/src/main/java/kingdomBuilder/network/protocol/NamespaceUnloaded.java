package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [NAMESPACE_UNLOADED] <#{namespace}>")
public record NamespaceUnloaded(
    String namespace
) {}
