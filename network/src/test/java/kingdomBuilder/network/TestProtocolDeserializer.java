package kingdomBuilder.network;

import kingdomBuilder.network.generated.ProtocolDeserializer;

import kingdomBuilder.network.protocol.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        RequestClientsResponse typedPacket = assertInstanceOf(
                RequestClientsResponse.class, testConsumer.getObject()
        );

        final var expectedClients = List.of(
            new ClientData(4, "Ich", -1),
            new ClientData(42, "Du", 100)
        );

        assertEquals(expectedClients, typedPacket.clients());
    }

    @Test
    void testDeserializingGameHosted() {
        final String packet = "[SERVER_MESSAGE] [GAME_HOSTED] <[1;kingdom_builder:KB;0;Mein Spiel;greatest Spiel ever;2;0]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        GameHosted typedPacket = assertInstanceOf(GameHosted.class, testConsumer.getObject());

        assertEquals(1, typedPacket.clientId());
        assertEquals("kingdom_builder:KB", typedPacket.gameType());
        assertEquals(0, typedPacket.gameId());
        assertEquals("Mein Spiel", typedPacket.gameName());
        assertEquals("greatest Spiel ever", typedPacket.gameDescription());
        assertEquals(2, typedPacket.playerLimit());
        assertEquals(0, typedPacket.playersJoined());
    }

    @Test
    void testDeserializingYouAreRoot() {
        final String packet = "[SERVER_MESSAGE] [YOU_ARE_ROOT]";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        assertInstanceOf(YouAreRoot.class, testConsumer.getObject());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingNamespaceLoaded() {
        final String packet = "[SERVER_MESSAGE] [NAMESPACE_LOADED] <kingdom_builder>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        NamespaceLoaded typedPacket = assertInstanceOf(NamespaceLoaded.class, testConsumer.getObject());

        assertEquals("kingdom_builder", typedPacket.namespace());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingNamespaceUnloaded() {
        final String packet = "[SERVER_MESSAGE] [NAMESPACE_UNLOADED] <kingdom_builder>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        NamespaceUnloaded typedPacket = assertInstanceOf(NamespaceUnloaded.class, testConsumer.getObject());

        assertEquals("kingdom_builder", typedPacket.namespace());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingYouSpectateGame() {
        final String packet = "[SERVER_MESSAGE] [YOU_SPECTATE_GAME] <4711>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        YouSpectateGame typedPacket = assertInstanceOf(YouSpectateGame.class, testConsumer.getObject());

        assertEquals(4711, typedPacket.gameId());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingStoppedSpectating() {
        final String packet = "[SERVER_MESSAGE] [STOPPED_SPECTATING] <4711>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        StoppedSpectating typedPacket = assertInstanceOf(StoppedSpectating.class, testConsumer.getObject());

        assertEquals(4711, typedPacket.gameId());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingYouLeftGame() {
        final String packet = "[SERVER_MESSAGE] [YOU_LEFT_GAME] <4711>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        YouLeftGame typedPacket = assertInstanceOf(YouLeftGame.class, testConsumer.getObject());

        assertEquals(4711, typedPacket.gameId());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingWelcomeToGame() {
        final String packet = "[SERVER_MESSAGE] [WELCOME_TO_GAME] <4711>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        WelcomeToGame typedPacket = assertInstanceOf(WelcomeToGame.class, testConsumer.getObject());

        assertEquals(4711, typedPacket.gameId());
    }

    @Test
    void testDeserializingPlayerJoined() {
        final String packet = "[SERVER_MESSAGE] [PLAYER_JOINED] <[42;5]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        PlayerJoined typedPacket = assertInstanceOf(PlayerJoined.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
        assertEquals(5, typedPacket.gameId());
    }

    @Test
    void testDeserializingPlayerLeft() {
        final String packet = "[SERVER_MESSAGE] [PLAYER_LEFT] <[42;5]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        PlayerLeft typedPacket = assertInstanceOf(PlayerLeft.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
        assertEquals(5, typedPacket.gameId());
    }

    @Test
    void testDeserializingTurnEndedByServer() {
        final String packet = "[SERVER_MESSAGE] [TURN_ENDED_BY_SERVER]";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        assertInstanceOf(TurnEndedByServer.class, testConsumer.getObject());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingVersionReply() {
        final String packet = "[REPLY_MESSAGE] (?version) <ws2021_v1.2.6>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        VersionReply typedPacket = assertInstanceOf(VersionReply.class, testConsumer.getObject());

        assertEquals("ws2021_v1.2.6", typedPacket.serverVersion());
    }

    @Test
    void testDeserializingWhoAmIReply() {
        final String packet = "[REPLY_MESSAGE] (?whoami) <[4;Ich;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        WhoAmIReply typedPacket = assertInstanceOf(WhoAmIReply.class, testConsumer.getObject());

        assertEquals(4, typedPacket.clientId());
        assertEquals("Ich", typedPacket.clientName());
        assertEquals(-1, typedPacket.gameId());
    }

    @Test
    void testDeserializingClientReply() {
        final String packet = "[REPLY_MESSAGE] (?client) <[4;Ich;-1]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        ClientReply typedPacket = assertInstanceOf(ClientReply.class, testConsumer.getObject());

        assertEquals(4, typedPacket.clientId());
        assertEquals("Ich", typedPacket.clientName());
        assertEquals(-1, typedPacket.gameId());
    }

    @Disabled("Disabled until parsing lists is fixed")
    @Test
    void testDeserializingGamesReply() {
        final String packet = "[REPLY_MESSAGE] (?games) <{[3;kingdom_builder:KB;0;a game;some game;2;0]}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        GamesReply typedPacket = assertInstanceOf(GamesReply.class, testConsumer.getObject());

        final var expectedGames = List.of(
                new GameData(3, "kingdom_builder:KB", 0, "a game",
                        "some game", 2, 0));

        assertEquals(expectedGames, typedPacket.games());
    }

    @Test
    void testDeserializingPlayersOfGameReply() {
        final String packet = "[REPLY_MESSAGE] (?playersofgame) <[21;{7,11,3}]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        PlayersOfGameReply typedPacket = assertInstanceOf(PlayersOfGameReply.class, testConsumer.getObject());

        final var expectedClientIds = List.of(7, 11, 3);

        assertEquals(21, typedPacket.gameId());
        assertEquals(expectedClientIds, typedPacket.clientIds());
    }

    @Disabled("Disabled until parsing lists is fixed")
    @Test
    void testDeserializingModulesReply() {
        final String packet = "[REPLY_MESSAGE] (?modules) <{[Server;std],[Kingdom Builder;kingdom_builder]}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        ModulesReply typedPacket = assertInstanceOf(ModulesReply.class, testConsumer.getObject());

        final var expectedModules = List.of(
                new ModuleData("Server", "std"),
                new ModuleData("Kingdom Builder", "kingdom_builder")
        );

        assertEquals(expectedModules, typedPacket.modules());
    }

    @Disabled("Disabled until parsing lists is fixed")
    @Test
    void testDeserializingMyNamespacesReply() {
        final String packet = "[REPLY_MESSAGE] (?mymodules) <{[Server;std],[Kingdom Builder;kingdom_builder]}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        MyNamespacesReply typedPacket = assertInstanceOf(MyNamespacesReply.class, testConsumer.getObject());

        final var expectedModules = List.of(
                new ModuleData("Server", "std"),
                new ModuleData("Kingdom Builder", "kingdom_builder")
        );

        assertEquals(expectedModules, typedPacket.modules());
    }

    @Disabled("Disabled until the ProtocolDeserializer.split() and the storage of single input parameter is fixed.")
    @Test
    void testDeserializingDetailsOfGameReply() {
        final String packet = "[REPLY_MESSAGE] (?detailsofgame) <???>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        DetailsOfGameReply typedPacket = assertInstanceOf(DetailsOfGameReply.class, testConsumer.getObject());

        assertEquals("???", typedPacket.details());
    }
}
