package org.royjacobs.lazybot.unit.server;

import org.junit.Test;
import org.royjacobs.lazybot.App;
import org.royjacobs.lazybot.hipchat.server.Paths;
import ratpack.impose.ImpositionsSpec;
import ratpack.impose.ServerConfigImposition;
import ratpack.server.RatpackServer;
import ratpack.test.ServerBackedApplicationUnderTest;

import java.util.Collections;

public class AppTest {
    public class TestServer extends ServerBackedApplicationUnderTest {
        @Override
        protected RatpackServer createServer() throws Exception {
            return App.createServer(new String[0]);
        }

        @Override
        protected void addImpositions(ImpositionsSpec impositions) {
            super.addImpositions(impositions);
            impositions.add(ServerConfigImposition.of(s -> s.props(Collections.singletonMap("server.publicAddress", "publicAddress"))));
        }
    }

    @Test
    public void canCallPaths() throws Exception {
        try (final TestServer server = new TestServer()) {
            server.createServer();
            server.test(t -> t.get(Paths.PATH_CAPABILITIES));
            server.test(t -> t.delete(Paths.PATH_INSTALL + "/unknown-oauth-id"));
            server.test(t -> t.post(Paths.PATH_WEBHOOK_ROOM_MESSAGE));
        }
    }
}
