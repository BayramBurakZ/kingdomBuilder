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
    void testSerializingKick() {
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

}
