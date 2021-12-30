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

    @Disabled
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

    @Test
    void testDeserializingInitStart(){
        final String packet = "[GAME_MESSAGE] [INIT_START]";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        assertInstanceOf(InitStart.class, testConsumer.getObject());
    }

    @Test
    void testDeserializingWinCondition() {
        final String packet = "[GAME_MESSAGE] [WIN_CONDITION] <[MINDER;FISHER;KNIGHT]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        WinCondition typedPacket = assertInstanceOf(WinCondition.class, testConsumer.getObject());

        assertEquals("MINDER", typedPacket.winCondition1());
        assertEquals("FISHER", typedPacket.winCondition2());
        assertEquals("KNIGHT", typedPacket.winCondition3());
    }

    @Test
    void testDeserializingGameStart() {
        final String packet = "[GAME_MESSAGE] [GAME_START]";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        assertInstanceOf(GameStart.class, testConsumer.getObject());
    }

    @Disabled("Disabled until parsing is fixed")
    @Test
    void testDeserializingYourTerrainCard(){
        final String packet = "[GAME_MESSAGE] [YOUR_TERRAIN_CARD] <GRAS>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        YourTerrainCard typedPacket = assertInstanceOf(YourTerrainCard.class, testConsumer.getObject());

        assertEquals("GRAS", typedPacket.terrainType());
    }

    @Disabled("Disabled until parsing is fixed")
    @Test
    void testDeserializingTurnStart() {
        final String packet = " [GAME_MESSAGE] [TURN_START] <42>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        TurnStart typedPacket = assertInstanceOf(TurnStart.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
    }

    @Disabled("Disabled until parsing is fixed")
    @Test
    void testDeserializingTerrainTypeOfTurn(){
        final String packet = "[GAME_MESSAGE] [TERRAIN_TYPE_OF_TURN] <GRAS>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        TerrainTypeOfTurn typedPacket = assertInstanceOf(TerrainTypeOfTurn.class, testConsumer.getObject());

        assertEquals("GRAS", typedPacket.terrainType());
    }

    @Test
    void testDeserializingSettlementPlaced(){
        final String packet = "[GAME_MESSAGE] [SETTLEMENT_PLACED] <[42;10;9]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        SettlementPlaced typedPacket = assertInstanceOf(SettlementPlaced.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
        assertEquals(10, typedPacket.row());
        assertEquals(9, typedPacket.column());
    }

    @Test
    void testDeserializingSettlementRemoved(){
        final String packet = "[GAME_MESSAGE] [SETTLEMENT_REMOVED] <[42;10;9]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        SettlementRemoved typedPacket = assertInstanceOf(SettlementRemoved.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
        assertEquals(10, typedPacket.row());
        assertEquals(9, typedPacket.column());
    }

    @Test
    void testDeserializingTokenReceived(){
        final String packet = "[GAME_MESSAGE] [TOKEN_RECEIVED] <[42;FARM;10;9]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        TokenReceived typedPacket = assertInstanceOf(TokenReceived.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
        assertEquals("FARM", typedPacket.tokenType());
        assertEquals(10, typedPacket.row());
        assertEquals(9, typedPacket.column());
    }

    @Test
    void testDeserializingTokenLost(){
        final String packet = "[GAME_MESSAGE] [TOKEN_LOST] <[42;FARM;10;9]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        TokenLost typedPacket = assertInstanceOf(TokenLost.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
        assertEquals("FARM", typedPacket.tokenType());
        assertEquals(10, typedPacket.row());
        assertEquals(9, typedPacket.column());
    }

    @Disabled("Disabled until parsing fixed")
    @Test
    void testDeserializingPlayerUsedLastSettlement(){
        final String packet = "[GAME_MESSAGE] [PLAYER_USED_LAST_SETTLEMENT] <42>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        PlayerUsedLastSettlement typedPacket = assertInstanceOf(PlayerUsedLastSettlement.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
    }

    @Disabled("Disabled until parsing fixed")
    @Test
    void testDeserializingGameOver(){
        final String packet = "[GAME_MESSAGE] [GAME_OVER] <{1,24,78,156,8}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        GameOver typedPacket = assertInstanceOf(GameOver.class, testConsumer.getObject());

        final var expectedClientIds = List.of(1, 24, 78, 156, 8);

        assertEquals(expectedClientIds, typedPacket.clientIds());
    }

    @Disabled("Disabled until Parsing is fixed")
    @Test
    void testDeserializingScores() {
        final String packet = "[GAME_MESSAGE] [SCORES] <{[4;54],[2;34]}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        Scores typedPacket = assertInstanceOf(Scores.class, testConsumer.getObject());

        final var expectedScoresData = List.of(
                new ScoresData(4, 54),
                new ScoresData(2, 34)
        );

        assertEquals(expectedScoresData, typedPacket.scoresDataList());
    }

    @Test
    void testDeserializingQuadrantUploaded(){
        final String packet = "[SERVER_MESSAGE] [QUADRANT_UPLOADED] <[3;4]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        QuadrantUploaded typedPacket = assertInstanceOf(QuadrantUploaded.class, testConsumer.getObject());

        assertEquals(3, typedPacket.clientId());
        assertEquals(4, typedPacket.quadrantId());
    }

    @Test
    void testDeserializingQuadrantReply() {
        final String packet = "[REPLY_MESSAGE] (?quadrant) <[45;WATER;WATER;WATER;FORREST;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER]>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        QuadrantReply typedPacket = assertInstanceOf(QuadrantReply.class, testConsumer.getObject());

        assertEquals(45, typedPacket.quadrantId());
        assertEquals("WATER", typedPacket.fieldType1());
        assertEquals("WATER", typedPacket.fieldType2());
        assertEquals("WATER", typedPacket.fieldType3());
        assertEquals("FORREST", typedPacket.fieldType4());
        assertEquals("WATER", typedPacket.fieldType5());
        assertEquals("WATER", typedPacket.fieldType6());
        assertEquals("WATER", typedPacket.fieldType7());
        assertEquals("WATER", typedPacket.fieldType8());
        assertEquals("WATER", typedPacket.fieldType9());
        assertEquals("WATER", typedPacket.fieldType10());
        assertEquals("WATER", typedPacket.fieldType11());
        assertEquals("WATER", typedPacket.fieldType12());
        assertEquals("WATER", typedPacket.fieldType13());
        assertEquals("WATER", typedPacket.fieldType14());
        assertEquals("WATER", typedPacket.fieldType15());
        assertEquals("WATER", typedPacket.fieldType16());
        assertEquals("WATER", typedPacket.fieldType17());
        assertEquals("WATER", typedPacket.fieldType18());
        assertEquals("WATER", typedPacket.fieldType19());
        assertEquals("WATER", typedPacket.fieldType20());
        assertEquals("WATER", typedPacket.fieldType21());
        assertEquals("WATER", typedPacket.fieldType22());
        assertEquals("WATER", typedPacket.fieldType23());
        assertEquals("WATER", typedPacket.fieldType24());
        assertEquals("WATER", typedPacket.fieldType25());
        assertEquals("WATER", typedPacket.fieldType26());
        assertEquals("WATER", typedPacket.fieldType27());
        assertEquals("WATER", typedPacket.fieldType28());
        assertEquals("WATER", typedPacket.fieldType29());
        assertEquals("WATER", typedPacket.fieldType30());
        assertEquals("WATER", typedPacket.fieldType31());
        assertEquals("WATER", typedPacket.fieldType32());
        assertEquals("WATER", typedPacket.fieldType33());
        assertEquals("WATER", typedPacket.fieldType34());
        assertEquals("WATER", typedPacket.fieldType35());
        assertEquals("WATER", typedPacket.fieldType36());
        assertEquals("WATER", typedPacket.fieldType37());
        assertEquals("WATER", typedPacket.fieldType38());
        assertEquals("WATER", typedPacket.fieldType39());
        assertEquals("WATER", typedPacket.fieldType40());
        assertEquals("WATER", typedPacket.fieldType41());
        assertEquals("WATER", typedPacket.fieldType42());
        assertEquals("WATER", typedPacket.fieldType43());
        assertEquals("WATER", typedPacket.fieldType44());
        assertEquals("WATER", typedPacket.fieldType45());
        assertEquals("WATER", typedPacket.fieldType46());
        assertEquals("WATER", typedPacket.fieldType47());
        assertEquals("WATER", typedPacket.fieldType48());
        assertEquals("WATER", typedPacket.fieldType49());
        assertEquals("WATER", typedPacket.fieldType50());
        assertEquals("WATER", typedPacket.fieldType51());
        assertEquals("WATER", typedPacket.fieldType52());
        assertEquals("WATER", typedPacket.fieldType53());
        assertEquals("WATER", typedPacket.fieldType54());
        assertEquals("WATER", typedPacket.fieldType55());
        assertEquals("WATER", typedPacket.fieldType56());
        assertEquals("WATER", typedPacket.fieldType57());
        assertEquals("WATER", typedPacket.fieldType58());
        assertEquals("WATER", typedPacket.fieldType59());
        assertEquals("WATER", typedPacket.fieldType60());
        assertEquals("WATER", typedPacket.fieldType61());
        assertEquals("WATER", typedPacket.fieldType62());
        assertEquals("WATER", typedPacket.fieldType63());
        assertEquals("WATER", typedPacket.fieldType64());
        assertEquals("WATER", typedPacket.fieldType65());
        assertEquals("WATER", typedPacket.fieldType66());
        assertEquals("WATER", typedPacket.fieldType67());
        assertEquals("WATER", typedPacket.fieldType68());
        assertEquals("WATER", typedPacket.fieldType69());
        assertEquals("WATER", typedPacket.fieldType70());
        assertEquals("WATER", typedPacket.fieldType71());
        assertEquals("WATER", typedPacket.fieldType72());
        assertEquals("WATER", typedPacket.fieldType73());
        assertEquals("WATER", typedPacket.fieldType74());
        assertEquals("WATER", typedPacket.fieldType75());
        assertEquals("WATER", typedPacket.fieldType76());
        assertEquals("WATER", typedPacket.fieldType77());
        assertEquals("WATER", typedPacket.fieldType78());
        assertEquals("WATER", typedPacket.fieldType79());
        assertEquals("WATER", typedPacket.fieldType80());
        assertEquals("WATER", typedPacket.fieldType81());
        assertEquals("WATER", typedPacket.fieldType82());
        assertEquals("WATER", typedPacket.fieldType83());
        assertEquals("WATER", typedPacket.fieldType84());
        assertEquals("WATER", typedPacket.fieldType85());
        assertEquals("WATER", typedPacket.fieldType86());
        assertEquals("WATER", typedPacket.fieldType87());
        assertEquals("WATER", typedPacket.fieldType88());
        assertEquals("WATER", typedPacket.fieldType89());
        assertEquals("WATER", typedPacket.fieldType90());
        assertEquals("WATER", typedPacket.fieldType91());
        assertEquals("WATER", typedPacket.fieldType92());
        assertEquals("WATER", typedPacket.fieldType93());
        assertEquals("WATER", typedPacket.fieldType94());
        assertEquals("WATER", typedPacket.fieldType95());
        assertEquals("WATER", typedPacket.fieldType96());
        assertEquals("WATER", typedPacket.fieldType97());
        assertEquals("WATER", typedPacket.fieldType98());
        assertEquals("WATER", typedPacket.fieldType99());
        assertEquals("WATER", typedPacket.fieldType100());
    }

    @Disabled("Disabled until parsing fixed")
    @Test
    void testDeserializingQuadrantsReply(){
        final String packet = "[REPLY_MESSAGE] (?quadrants) <{0;1;4;7}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        QuadrantsReply typedPacket = assertInstanceOf(QuadrantsReply.class, testConsumer.getObject());

        final var expectedQuadrantsIds = List.of(0, 1, 4, 7);

        assertEquals(expectedQuadrantsIds, typedPacket.quadrantIds());
    }

    @Disabled
    @Test
    void testDeserializingTimeLimitReply() {
        final String packet = "[REPLY_MESSAGE] (?timelimit) <42000>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        TimeLimitReply typedPacket = assertInstanceOf(TimeLimitReply.class, testConsumer.getObject());

        assertEquals(42000, typedPacket.timeLimit());
    }

    @Disabled
    @Test
    void testDeserializingPlayerLimitReply() {
        final String packet = "[REPLY_MESSAGE] (?playerlimit) <4>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        PlayerLimitReply typedPacket = assertInstanceOf(PlayerLimitReply.class, testConsumer.getObject());

        assertEquals(4, typedPacket.playerLimit());
    }

    @Disabled
    @Test
    void testDeserializingPlayersReply() {
        final String packet = "[REPLY_MESSAGE] (?players) <{[21;RED],[42;BLUE]}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        PlayersReply typedPacket = assertInstanceOf(PlayersReply.class, testConsumer.getObject());

        final var expectedPlayersData = List.of(
                new PlayerData(21, "RED"),
                new PlayerData(42, "BLUE")
        );

        assertEquals(expectedPlayersData, typedPacket.playerDataList());
    }

    @Disabled
    @Test
    void testDeserializingTurnsReply() {
        final String packet = "[REPLY_MESSAGE] (?turns) <21>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        TurnsReply typedPacket = assertInstanceOf(TurnsReply.class, testConsumer.getObject());

        assertEquals(21, typedPacket.turns());
    }

    @Disabled
    @Test
    void testDeserializingTurnLimitReply() {
        final String packet = "[REPLY_MESSAGE] (?turnlimit) <100>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        TurnLimitReply typedPacket = assertInstanceOf(TurnLimitReply.class, testConsumer.getObject());

        assertEquals(100, typedPacket.turnLimit());
    }

    @Disabled
    @Test
    void testDeserializingWhoseTurnReply() {
        final String packet = "[REPLY_MESSAGE] (?whoseturn) <42>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        WhoseTurnReply typedPacket = assertInstanceOf(WhoseTurnReply.class, testConsumer.getObject());

        assertEquals(42, typedPacket.clientId());
    }

    @Disabled
    @Test
    void testDeserializingSettlementsLeftReply(){
        final String packet = "[REPLY_MESSAGE] (?settlementsleft) <{[1;16],[2;14]}>";
        ProtocolDeserializer.deserialize(packet, testConsumer);

        assertFalse(testConsumer.hasError());
        SettlementsLeftReply typedPacket = assertInstanceOf(SettlementsLeftReply.class, testConsumer.getObject());

        final var expectedSettlementData = List.of(
                new SettlementData(1, 16),
                new SettlementData(2, 14)
        );

        assertEquals(expectedSettlementData, typedPacket.settlementsDataList());
    }
}
