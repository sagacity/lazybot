package org.royjacobs.lazybot;

import com.google.common.io.Resources;
import ratpack.impose.ImpositionsSpec;
import ratpack.impose.ServerConfigImposition;
import ratpack.server.RatpackServer;
import ratpack.test.ServerBackedApplicationUnderTest;

public class TestServer extends ServerBackedApplicationUnderTest {
    @Override
    protected RatpackServer createServer() throws Exception {
        return App.createServer(new String[]{});
    }

    @Override
    protected void addImpositions(ImpositionsSpec impositions) {
        super.addImpositions(impositions);
        //impositions.add(ServerConfigImposition.of(s -> s.json(Resources.getResource("config-test.json"))));
    }
}
