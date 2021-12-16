package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "iam #{name}")
public record IAm(
    String name
) {
}
