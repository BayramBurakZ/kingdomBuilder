package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

import java.util.List;

@Protocol(format = "[REPLY_MESSAGE] (?quadrants) <{#{quadrantIds}}>")
public record QuadrantsReply(
        List<Integer> quadrantIds
) {
}
