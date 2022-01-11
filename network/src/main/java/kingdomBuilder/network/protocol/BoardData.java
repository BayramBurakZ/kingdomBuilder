package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "[#{quadrantId1};#{quadrantId2};#{quadrantId3};#{quadrantId4}]", isComponent = true)
public record BoardData(
    int quadrantId1,
    int quadrantId2,
    int quadrantId3,
    int quadrantId4
) {}
