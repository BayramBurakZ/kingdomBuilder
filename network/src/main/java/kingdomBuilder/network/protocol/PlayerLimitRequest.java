package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?playerlimit")
public record PlayerLimitRequest() {
}
