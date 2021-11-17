package kingdomBuilder.network;

import kingdomBuilder.network.internal.ProtocolMapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestProtocolMapper {

    public void testGetPlaceholders() {
        var placeholders = ProtocolMapper.getPlaceholders("#{a} #{b}");

        assertArrayEquals(placeholders.toArray(), new String[]{"a", "b"});
    }

}
