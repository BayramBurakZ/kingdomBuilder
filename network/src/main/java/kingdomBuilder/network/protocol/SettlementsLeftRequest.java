package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests the number of the settlements left.
 */
@Protocol(format = "?settlementsleft")
public record SettlementsLeftRequest() {
}
