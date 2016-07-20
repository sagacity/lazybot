package com.bol.lazybot.server.capability;

import com.bol.lazybot.server.capability.dto.*;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.server.ServerConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

import static ratpack.jackson.Jackson.json;

@Singleton
public class CapabilityHandler implements Handler {
    private final ServerConfig serverConfig;

    @Inject
    public CapabilityHandler(final ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public void handle(Context context) throws Exception {
        final String selfUrl = serverConfig.getPublicAddress().resolve("/capabilities").toString();
        final String callbackUrl = serverConfig.getPublicAddress().resolve("/installed").toString();

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
