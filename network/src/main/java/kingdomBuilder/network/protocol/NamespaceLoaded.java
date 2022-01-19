package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when a namespace has been loaded.
 */
@Protocol(format = "[SERVER_MESSAGE] [NAMESPACE_LOADED] <#{namespace}>")
public record NamespaceLoaded(
        String namespace
) {}
