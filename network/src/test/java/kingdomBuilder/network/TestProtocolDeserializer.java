package kingdomBuilder.network;

import kingdomBuilder.network.generated.ProtocolDeserializer;

import kingdomBuilder.network.protocol.*;
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
        ClientLeft typedPacket = assertInstanceOf(ClientLeft.class, testConsumer.getObject());

        assertEquals(4, typedPacket.clientId());
        assertEquals("Ich", typedPacket.name());
        assertEquals(-1, typedPacket.gameId());
    }

    @Test
    void testParsingClientJoined() {
        final String packet = "[SERVER_MESSAGE] [CLIENT_JOINED] <[4;Ich;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        ClientJoined typedPacket = assertInstanceOf(ClientJoined.class, testConsumer.getObject());

        assertEquals(4, typedPacket.clientId());
        assertEquals("Ich", typedPacket.name());
        assertEquals(-1, typedPacket.gameId());
    }

    @Test
    void testParsingClientJoinedWithSpacesName() {
        final String packet = "[SERVER_MESSAGE] [CLIENT_JOINED] <[4; H a h;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        ClientJoined typedPacket = assertInstanceOf(ClientJoined.class, testConsumer.getObject());

        assertEquals(4, typedPacket.clientId());
        assertEquals(" H a h", typedPacket.name());
        assertEquals(-1, typedPacket.gameId());
    }

    @Test
    void testParsingMessage() {
        final String packet = "[SERVER_MESSAGE] [MESSAGE] <[1;{2,3,42};Hallo Du!]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        Message typedPacket = assertInstanceOf(Message.class, testConsumer.getObject());

        assertEquals(1, typedPacket.clientId());
        assertEquals(List.of(2, 3, 42), typedPacket.receiverIds());
        assertEquals("Hallo Du!", typedPacket.message());
    }

    @Test
    void testParsingWelcomeToServer() {
        final String packet = "[SERVER_MESSAGE] [WELCOME_TO_SERVER] <[4;Test;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        WelcomeToServer typedPacket = assertInstanceOf(
                WelcomeToServer.class, testConsumer.getObject()
        );

        assertEquals(4, typedPacket.clientId());
        assertEquals("Test", typedPacket.name());
        assertEquals(-1, typedPacket.gameId());
    }

    @Test
    void testParsingRequestClientsResponse() {
        final String packet = "[REPLY MESSAGE] (?clients) <{[4;Ich;-1],[42;Du;100]}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError(), "Parsing failed with an error.");
        RequestClientsResponse typePacket = assertInstanceOf(
                RequestClientsResponse.class, testConsumer.getObject()
        );

        final var expectedClients = List.of(
            new ClientData(4, "Ich", -1),
            new ClientData(42, "Du", 100)
        );

        assertEquals(expectedClients, typePacket.clients());
    }

}
