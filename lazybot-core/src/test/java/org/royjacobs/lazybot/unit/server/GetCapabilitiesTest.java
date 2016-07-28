package org.royjacobs.lazybot.unit.server;

import org.junit.Test;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import org.royjacobs.lazybot.utils.JacksonUtils;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.ServerConfig;
import ratpack.test.embed.EmbeddedApp;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

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

        EmbeddedApp.fromHandlers(chain -> chain
                .get(new GetCapabilitiesHandler(serverConfig, hipChatConfig))
        ).test(client -> {
            final ReceivedResponse response = client.get();
            final Map caps = JacksonUtils.deserialize(response.getBody().getText(), Map.class);
            assertThat(caps.get("name"), is("LazyBot"));
        });

    }
}
