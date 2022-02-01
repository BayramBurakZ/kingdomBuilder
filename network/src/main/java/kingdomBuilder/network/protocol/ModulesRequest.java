package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests all registered modules und their namespaces.
 */
@Protocol(format = "?modules")
public record ModulesRequest() {
}
