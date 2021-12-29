package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "root #{password}")
public record Root(
        String password
) {
}
