package org.royjacobs.lazybot.hipchat.server.capabilities;

import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.server.Paths;
import org.royjacobs.lazybot.hipchat.server.capabilities.dto.*;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.server.ServerConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

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
        final String selfUrl = serverConfig.getPublicAddress().resolve("/" + Paths.PATH_CAPABILITIES).toString();
        final String callbackUrl = serverConfig.getPublicAddress().resolve("/" + Paths.PATH_INSTALL).toString();
        final String messageUrl = serverConfig.getPublicAddress().resolve("/" + Paths.PATH_WEBHOOK_ROOM_MESSAGE).toString();

        final Capabilities caps = Capabilities.builder()
                .name("LazyBot")
                .description("Awesome LazyBot!")
                .key("org.royjacobs.lazybot")
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
                                .webhook(WebHook.builder()
                                        .name("messages")
                                        .event("room_message")
                                        .url(messageUrl)
                                        .authentication("none")
                                        .pattern("^/[lL][aA][zZ][yY][bB][oO][tT]")
                                        .build()
                                )
                                .build()
                )
                .build();

        context.render(json(caps));
    }
}
