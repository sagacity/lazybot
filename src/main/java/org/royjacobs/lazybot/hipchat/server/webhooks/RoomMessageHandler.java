package org.royjacobs.lazybot.hipchat.server.webhooks;

import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.hipchat.server.webhooks.dto.RoomMessage;
import lombok.extern.slf4j.Slf4j;
import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

import static ratpack.jackson.Jackson.fromJson;

@Slf4j
public class RoomMessageHandler extends InjectionHandler {
    public void handle(final Context ctx, final BotOrchestrationService botOrchestrationService) throws Exception {
        ctx.byMethod(m -> m
                .post(() -> ctx.parse(Jackson.fromJson(RoomMessage.class))
                        .next(botOrchestrationService::onRoomMessage)
                        .then(msg -> ctx.getResponse().status(Status.OK).send())
                )
        );
    }
}
