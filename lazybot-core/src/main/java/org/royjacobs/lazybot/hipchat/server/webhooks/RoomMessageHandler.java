package org.royjacobs.lazybot.hipchat.server.webhooks;

import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.hipchat.server.validator.HipChatRequestValidator;
import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;
import ratpack.http.Status;
import ratpack.jackson.Jackson;

@Slf4j
public class RoomMessageHandler extends InjectionHandler {
    public void handle(final Context ctx, final BotOrchestrationService botOrchestrationService, final HipChatRequestValidator requestValidator) throws Exception {
        ctx.byMethod(m -> m
                .post(() -> ctx.parse(Jackson.fromJson(RoomMessage.class))
                        .next(msg -> botOrchestrationService.getActiveInstallationByOauthId(msg.getOauthId()).ifPresent(installation -> requestValidator.validate(ctx.getRequest(), installation.getOauthSecret())))
                        .next(botOrchestrationService::onRoomMessage)
                        .then(msg -> ctx.getResponse().status(Status.OK).send())
                )
        );
    }
}
