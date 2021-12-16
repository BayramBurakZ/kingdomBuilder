package kingdomBuilder.network;

import kingdomBuilder.network.generated.ProtocolDeserializer;

import kingdomBuilder.network.protocol.ClientJoined;
import kingdomBuilder.network.protocol.ClientLeft;
import kingdomBuilder.network.protocol.Message;
import kingdomBuilder.network.protocol.WelcomeToServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestProtocolDeserializer {
    ProtocolTestConsumer testConsumer;

    @BeforeEach
    void setUp() {
        testConsumer = new ProtocolTestConsumer();
    }

    @Test
    void testParsingClientLeft() {
        final String packet = "[SERVER_MESSAGE] [CLIENT_LEFT] <[4;Ich;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        assertInstanceOf(ClientLeft.class, testConsumer.getObject());

        ClientLeft typedPacket = (ClientLeft) testConsumer.getObject();
        assertEquals(4, typedPacket.clientId());
        assertEquals("Ich", typedPacket.name());
        assertEquals(-1, typedPacket.gameId());
    }

    @Test
    void testParsingClientJoined() {
        final String packet = "[SERVER_MESSAGE] [CLIENT_JOINED] <[4;Ich;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        assertInstanceOf(ClientJoined.class, testConsumer.getObject());

        ClientJoined typedPacket = (ClientJoined) testConsumer.getObject();
        assertEquals(4, typedPacket.clientId());
        assertEquals("Ich", typedPacket.name());
        assertEquals(-1, typedPacket.gameId());
    }

    @Test
    void testParsingMessage() {
        final String packet = "[SERVER_MESSAGE] [MESSAGE] <[1;{2,3,42};Hallo Du!]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        assertInstanceOf(Message.class, testConsumer.getObject());

        Message typedPacket = (Message) testConsumer.getObject();
        assertEquals(1, typedPacket.clientId());
        assertEquals(List.of(2, 3, 42), typedPacket.receiverIds());
        assertEquals("Hallo Du!", typedPacket.message());
    }

    @Test
    void testParsingWelcomeToServer() {
        final String packet = "[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[4;Test;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        assertInstanceOf(WelcomeToServer.class, testConsumer.getObject());

        WelcomeToServer typedPacket = (WelcomeToServer) testConsumer.getObject();
        assertEquals(4, typedPacket.clientId());
        assertEquals("Test", typedPacket.name());
        assertEquals(-1, typedPacket.gameId());
    }

}
