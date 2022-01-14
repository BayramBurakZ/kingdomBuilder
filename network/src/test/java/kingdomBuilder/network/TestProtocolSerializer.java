package kingdomBuilder.network;

import kingdomBuilder.network.generated.ProtocolSerializer;

import kingdomBuilder.network.protocol.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestProtocolSerializer {

    @Test
    void testSerializingIAm() {
        final String message = ProtocolSerializer.serialize(new IAm("Dr.  Best"));
        assertEquals(message, "iam Dr.  Best");
    }

    @Test
    void testSerializingBye() {
        final String message = ProtocolSerializer.serialize(new Bye());
        assertEquals(message, "bye");
    }

    @Test
    void testSerializingPong() {
        final String message = ProtocolSerializer.serialize(new Pong());
        assertEquals("pong", message);
    }

    @Test
    void testSerializingLoad() {
        final String message = ProtocolSerializer.serialize(new Load("kingdom_builder"));
        assertEquals("load kingdom_builder", message);
    }

    @Test
    void testSerializingUnload() {
        final String message = ProtocolSerializer.serialize(new Unload("kingdom_builder"));
        assertEquals("unload kingdom_builder", message);
    }

    @Test
    void testSerializingJoin() {
        final String message = ProtocolSerializer.serialize(new Join(4711));
        assertEquals("join 4711", message);
    }

    @Test
    void testSerializingSpectate() {
        final String message = ProtocolSerializer.serialize(new Spectate(4711));
        assertEquals("spectate 4711", message);
    }

    @Test
    void testSerializingRoot() {
        final String message = ProtocolSerializer.serialize(new Root("*r00t*"));
        assertEquals("root *r00t*", message);
    }

    @Test
    void testSerializingUnspectate() {
        final String message = ProtocolSerializer.serialize(new Unspectate());
        assertEquals("unspectate", message);
    }

    @Test
    void testSerializingShutdownServer() {
        final String message = ProtocolSerializer.serialize(new ShutdownServer());
        assertEquals("shutdown server", message);
    }

    @Test
    void testSerializingKickClient() {
        final String message = ProtocolSerializer.serialize(new KickClient(42));
        assertEquals("kick client 42", message);
    }

    @Test
    void testSerializingChat() {
        final String message = ProtocolSerializer.serialize(new Chat(List.of(2, 3, 42), "Hallo Du!"));
        assertEquals("chat [{2,3,42};Hallo Du!]", message);
    }

    @Test
    void testSerializingVersionRequest() {
        final String message = ProtocolSerializer.serialize(new VersionRequest());
        assertEquals("?version", message);
    }

    @Test
    void testSerializingWhoAmIRequest() {
        final String message = ProtocolSerializer.serialize(new WhoAmIRequest());
        assertEquals("?whoami", message);
    }

    @Test
    void testSerializingClientRequest() {
        final String message = ProtocolSerializer.serialize(new ClientRequest(42));
        assertEquals("?client 42", message);
    }

    @Test
    void testSerializingClientsRequest() {
        final String message = ProtocolSerializer.serialize(new RequestClients());
        assertEquals("?clients", message);
    }

    @Test
    void testSerializingGamesRequest() {
        final String message = ProtocolSerializer.serialize(new GamesRequest());
        assertEquals("?games", message);
    }

    @Test
    void testSerializingPlayersOfGameRequest() {
        final String message = ProtocolSerializer.serialize(new PlayersOfGameRequest(4711));
        assertEquals("?playersofgame 4711", message);
    }

    @Test
    void testSerializingModulesRequest() {
        final String message = ProtocolSerializer.serialize(new ModulesRequest());
        assertEquals("?modules", message);
    }

    @Test
    void testSerializingMyModulesRequest() {
        final String message = ProtocolSerializer.serialize(new MyModulesRequest());
        assertEquals("?mymodules", message);
    }

    @Test
    void testSerializingDetailsOfGameRequest() {
        final String message = ProtocolSerializer.serialize(new DetailsOfGameRequest(4711));
        assertEquals("?detailsofgame 4711", message);
    }

    @Test
    void testSerializingQuadrantRequest() {
        final String message = ProtocolSerializer.serialize(new QuadrantRequest(5));
        assertEquals("?quadrant 5", message);
    }

    @Test
    void testSerializingQuadrantsRequest() {
        final String message = ProtocolSerializer.serialize(new QuadrantsRequest());
        assertEquals("?quadrants", message);
    }

    @Test
    void testSerializingUploadQuadrant() {
        final String message = ProtocolSerializer.serialize(new UploadQuadrant("WATER", "WATER", "WATER", "FORREST",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER",
                "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER", "WATER"));
        assertEquals("upload quadrant [WATER;WATER;WATER;FORREST;WATER;WATER;WATER;WATER;WATER;" +
        "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;WATER;" +
                "WATER;WATER;WATER;WATER;WATER;WATER]", message);
    }

    @Test
    void testSerializingHostGame() {
        final String message = ProtocolSerializer.serialize(new HostGame("my game", "some game",
                3, -1, -1, 0, 1, 2, 3));
        assertEquals("host game [my game;some game;3;-1;-1;[0;1;2;3]]", message);
    }

    @Test
    void testSerializingTimeLimitRequest() {
        final String message = ProtocolSerializer.serialize(new TimeLimitRequest());
        assertEquals("?timelimit", message);
    }

    @Test
    void testSerializingPlayerLimitRequest() {
        final String message = ProtocolSerializer.serialize(new PlayerLimitRequest());
        assertEquals("?playerlimit", message);
    }

    @Test
    void testSerializingPlayersRequest() {
        final String message = ProtocolSerializer.serialize(new PlayersRequest());
        assertEquals("?players", message);
    }

    @Test
    void testSerializingTurnsRequest() {
        final String message = ProtocolSerializer.serialize(new TurnsRequest());
        assertEquals("?turns", message);
    }

    @Test
    void testSerializingTurnLimitRequest() {
        final String message = ProtocolSerializer.serialize(new TurnLimitRequest());
        assertEquals("?turnlimit", message);
    }

    @Test
    void testSerializingWhoseTurnRequest() {
        final String message = ProtocolSerializer.serialize(new WhoseTurnRequest());
        assertEquals("?whoseturn", message);
    }

    @Test
    void testSerializingSettlementsLeftRequest() {
        final String message = ProtocolSerializer.serialize(new SettlementsLeftRequest());
        assertEquals("?settlementsleft", message);
    }

    @Test
    void testSerializingBoardRequest() {
        final String message = ProtocolSerializer.serialize(new BoardRequest());
        assertEquals("?board", message);
    }

    @Test
    void testSerializingWinConditionsRequest() {
        final String message = ProtocolSerializer.serialize(new WinConditionsRequest());
        assertEquals("?winconditions", message);
    }

    @Test
    void testSerializingMyGameRequest() {
        final String message = ProtocolSerializer.serialize(new MyGameRequest());
        assertEquals("?mygame", message);
    }

    @Test
    void testSerializingEndTurn() {
        final String message = ProtocolSerializer.serialize(new EndTurn());
        assertEquals("end turn", message);
    }

    @Test
    void testSerializingPlace() {
        final String message = ProtocolSerializer.serialize(new Place(3, 4));
        assertEquals("place [3;4]", message);
    }

    @Test
    void testSerializingOracle() {
        final String message = ProtocolSerializer.serialize(new Oracle(3, 4));
        assertEquals("oracle [3;4]", message);
    }

    @Test
    void testSerializingFarm() {
        final String message = ProtocolSerializer.serialize(new Farm(3, 4));
        assertEquals("farm [3;4]", message);
    }

    @Test
    void testSerializingTavern() {
        final String message = ProtocolSerializer.serialize(new Tavern(3, 4));
        assertEquals("tavern [3;4]", message);
    }

    @Test
    void testSerializingTower() {
        final String message = ProtocolSerializer.serialize(new Tower(3, 4));
        assertEquals("tower [3;4]", message);
    }

    @Test
    void testSerializingOasis() {
        final String message = ProtocolSerializer.serialize(new Oasis(3, 4));
        assertEquals("oasis [3;4]", message);
    }

    @Test
    void testSerializingHarbor() {
        final String message = ProtocolSerializer.serialize(new Harbor(3, 4, 2, 1));
        assertEquals("harbor [3;4;2;1]", message);
    }

    @Test
    void testSerializingPaddock() {
        final String message = ProtocolSerializer.serialize(new Paddock(3, 4, 2, 1));
        assertEquals("paddock [3;4;2;1]", message);
    }

    @Test
    void testSerializingBarn() {
        final String message = ProtocolSerializer.serialize(new Barn(3, 4, 2, 1));
        assertEquals("barn [3;4;2;1]", message);
    }
}
