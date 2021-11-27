package kingdomBuilder.network;

import kingdomBuilder.network.internal.MessageFormatParser;
import kingdomBuilder.network.internal.MessageFormatRenderer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class TestMessageMapper {

    @Test
    public void testMappingIAm() {
        MessageFormatRenderer renderer = new MessageFormatRenderer();

        IAm msg = new IAm("Test");
        String str = renderer.render(msg);

        assertEquals(str, "iam Test");
    }

    @Test
    public void testMappingIAmResponse() {
        MessageFormatParser parser = new MessageFormatParser();
        String response_string = "[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[4;Test;-1]>";
        IAmResponse response = parser.parseTo(response_string, IAmResponse.class);

        assertNotNull(response);
        assertEquals(response.clientId(), 4);
        assertEquals(response.name(), "Test");
        assertEquals(response.gameId(), -1);
    }

}
