package org.royjacobs.lazybot.unit.server;

import org.junit.Test;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import org.royjacobs.lazybot.hipchat.server.capabilities.dto.Capabilities;
import ratpack.jackson.internal.DefaultJsonRender;
import ratpack.server.ServerConfig;
import ratpack.test.handling.RequestFixture;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GetCapabilitiesTest {
    @Test
    public void getCaps() throws Exception {
        final ServerConfig serverConfig = ServerConfig.of(c -> c
                .publicAddress(new URI("test"))
        );
        final HipChatConfig hipChatConfig = new HipChatConfig();
        hipChatConfig.setScopes(new HashSet<>(Arrays.asList("scope1", "scope2")));

        final Capabilities result = (Capabilities)RequestFixture.requestFixture()
                .handle(new GetCapabilitiesHandler(serverConfig, hipChatConfig)).rendered(DefaultJsonRender.class).getObject();
        assertThat(result.getName(), is("LazyBot"));
    }
}
