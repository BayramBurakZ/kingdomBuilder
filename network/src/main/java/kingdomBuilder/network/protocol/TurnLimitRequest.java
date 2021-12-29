package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?turnlimit")
public record TurnLimitRequest() {
}
