package com.bol.lazybot.hipchat.server.capabilities;

import com.bol.lazybot.HipChatConfig;
import com.bol.lazybot.hipchat.server.capabilities.dto.*;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.server.ServerConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bol.lazybot.hipchat.server.Paths.PATH_CAPABILITIES;
import static com.bol.lazybot.hipchat.server.Paths.PATH_INSTALL;
import static ratpack.jackson.Jackson.json;

@Singleton
public class GetCapabilitiesHandler implements Handler {
    private final ServerConfig serverConfig;
    private final HipChatConfig hipChatConfig;

    @Inject
    public GetCapabilitiesHandler(final ServerConfig serverConfig, final HipChatConfig hipChatConfig) {
        this.serverConfig = serverConfig;
        this.hipChatConfig = hipChatConfig;
    }

    @Override
    public void handle(Context context) throws Exception {
        final String selfUrl = serverConfig.getPublicAddress().resolve("/" + PATH_CAPABILITIES).toString();
        final String callbackUrl = serverConfig.getPublicAddress().resolve("/" + PATH_INSTALL).toString();

        final Capabilities caps = Capabilities.builder()
                .name("LazyBot")
                .description("Awesome LazyBot!")
                .key("com.bol.lazybot")
                .links(
                        Links.builder()
                                .self(selfUrl)
                            .build()
                )
                .capabilities(
                        CapabilitiesContent.builder()
                                .installable(
                                        Installable.builder()
                                                .allowGlobal(false)
                                                .allowRoom(true)
                                                .callbackUrl(callbackUrl)
                                                .build()
                                )
                                .hipChatApiConsumer(
                                        HipChatApiConsumer.builder()
                                                .scopes(hipChatConfig.getScopes())
                                                .build()
                                )
                                .build()
                )
                .build();

        context.render(json(caps));
    }
}
