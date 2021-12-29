package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

@Protocol(format = "upload quadrant [#{fieldTypes}]")
public record UploadQuadrant(
        String[] fieldTypes
) {
}
