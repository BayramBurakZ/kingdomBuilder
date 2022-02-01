package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

/**
 * Represents message that contains all IDs of all known quadrants -
 *  * reply message ?quadrants {@link kingdomBuilder.network.protocol.QuadrantsRequest}.
 */
@Protocol(format = "[REPLY_MESSAGE] (?quadrants) <{#{quadrantIds}}>")
public record QuadrantsReply(
        List<Integer> quadrantIds
) {
}
