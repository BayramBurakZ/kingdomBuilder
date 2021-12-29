package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "?timelimit")
public record TimeLimitRequest() {
}
