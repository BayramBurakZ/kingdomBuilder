package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that will be sent when a namespace has been unloaded.
 */
@Protocol(format = "[SERVER_MESSAGE] [NAMESPACE_UNLOADED] <#{namespace}>")
public record NamespaceUnloaded(
    String namespace
) {}
