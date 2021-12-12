package kingdomBuilder.network;

import kingdomBuilder.network.internal.MessageFormatParser;


import static org.junit.jupiter.api.Assertions.*;

import kingdomBuilder.network.protocol.server.ClientJoined;
import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.network.protocol.server.WelcomeToServer;
import org.junit.jupiter.api.Test;

public class TestMessageParser {

    @Test
    public void testParsingWelcomeToServer() {
        MessageFormatParser parser = new MessageFormatParser();
        String str = "[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[4;Test;-1]>";
        WelcomeToServer msg = parser.parseTo(str, WelcomeToServer.class);

        assertNotNull(msg);
        assertEquals(msg.clientId(), 4);
        assertEquals(msg.name(), "Test");
        assertEquals(msg.gameId(), -1);
    }

    @Test
    public void testParsingClientJoined() {
        MessageFormatParser parser = new MessageFormatParser();
        String str = "[SERVER_MESSAGE] [CLIENT_JOINED] <[4;Ich;-1]>";
        ClientJoined msg = parser.parseTo(str, ClientJoined.class);

        assertNotNull(msg);
        assertEquals(msg.clientId(), 4);
        assertEquals(msg.name(), "Ich");
        assertEquals(msg.gameId(), -1);
    }

    @Test
    public void testParsingMessage() {
        MessageFormatParser parser = new MessageFormatParser();
        String str = "[SERVER_MESSAGE] [MESSAGE] <[1;{2,3,42};Hallo Du!]>";
        Message msg = parser.parseTo(str, Message.class);

        assertNotNull(msg);
        assertEquals(msg.clientId(), 1);
        assertArrayEquals(msg.receiverIds(), new Integer[]{2, 3, 42});
        assertEquals(msg.message(), "Hallo Du!");
    }

    @Test
    public void testParsingMessageWithSpecialCharacters() {
        MessageFormatParser parser = new MessageFormatParser();
        String str = "[SERVER_MESSAGE] [MESSAGE] <[1;{2,3,42};Hallo []>{}Du!]>";
        Message msg = parser.parseTo(str, Message.class);

        assertNotNull(msg);
    }

}
