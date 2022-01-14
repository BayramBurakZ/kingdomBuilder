package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents message that requests IDs of all known quadrants.
 */
@Protocol(format = "?quadrants")
public record QuadrantsRequest() {
}
