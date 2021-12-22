package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[SERVER_MESSAGE] [NAMESPACE_LOADED] <#{namespace}>")
public record NamespaceLoaded(
        String namespace
) {}
