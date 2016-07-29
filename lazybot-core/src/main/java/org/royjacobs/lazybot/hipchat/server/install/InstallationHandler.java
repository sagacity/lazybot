package org.royjacobs.lazybot.hipchat.server.install;

import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.hipchat.server.install.dto.InstalledInformation;
import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;
import ratpack.http.Status;

import javax.inject.Singleton;

import static ratpack.jackson.Jackson.fromJson;

@Singleton
@Slf4j
public class InstallationHandler extends InjectionHandler {
    public void handle(final Context ctx, final BotOrchestrationService botOrchestrationService) throws Exception {
        ctx.byMethod(m -> m
                .post(() -> ctx.parse(fromJson(InstalledInformation.class))
                        .next(info -> {
                            log.debug("Received installation request: {}", info);
                            final Installation installation = Installation.builder()
                                    .oauthId(info.getOauthId())
                                    .oauthSecret(info.getOauthSecret())
                                    .roomId(info.getRoomId())
                                    .build();
                            botOrchestrationService.registerInstallation(installation);
                        })
                        .then(info -> ctx.getResponse().status(Status.OK).send()))
                .delete(() -> {
                    final String oauthId = ctx.getPathTokens().get("oauthid");
                    botOrchestrationService.getActiveInstallationByOauthId(oauthId).ifPresent(botOrchestrationService::unregisterInstallation);
                    ctx.getResponse().status(Status.OK).send();
                })
        );
    }
}
