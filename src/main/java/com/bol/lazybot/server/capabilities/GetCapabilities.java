package com.bol.lazybot.server.capabilities;

import com.bol.lazybot.server.capabilities.dto.*;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.server.ServerConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bol.lazybot.server.Paths.PATH_CAPABILITIES;
import static com.bol.lazybot.server.Paths.PATH_INSTALL;
import static ratpack.jackson.Jackson.json;

@Singleton
public class GetCapabilities implements Handler {
    private final ServerConfig serverConfig;

    @Inject
    public GetCapabilities(final ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
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
                                                .scope("send_notification")
                                                .scope("view_messages")
                                                .scope("view_room")
                                                .build()
                                )
                                .build()
                )
                .build();

        context.render(json(caps));
    }
}
