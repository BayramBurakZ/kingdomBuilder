package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the namespaces currently registered for the requesting client.
 */
@Protocol(format = "?mymodules")
public record MyModulesRequest() {
}
