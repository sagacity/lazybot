package com.bol.lazybot.hipchat.server.install;

import com.bol.lazybot.bot.BotOrchestrationService;
import com.bol.lazybot.hipchat.installations.Installation;
import com.bol.lazybot.hipchat.installations.InstallationRepository;
import com.bol.lazybot.hipchat.server.install.dto.InstalledInformation;
import lombok.extern.slf4j.Slf4j;
import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;
import ratpack.http.Status;

import javax.inject.Singleton;

import static ratpack.jackson.Jackson.fromJson;

@Singleton
@Slf4j
public class InstallationHandler extends InjectionHandler {
    public void handle(final Context ctx, final InstallationRepository installationRepository, final BotOrchestrationService botOrchestrationService) throws Exception {
        ctx.byMethod(m -> m
                .post(() -> ctx.parse(fromJson(InstalledInformation.class))
                        .next(info -> {
                            log.debug("Received installation request: {}", info);
                            final Installation installation = Installation.builder()
                                    .oauthId(info.getOauthId())
                                    .oauthSecret(info.getOauthSecret())
                                    .roomId(info.getRoomId())
                                    .build();
                            installationRepository.save(installation);
                            botOrchestrationService.startInstallation(installation);
                        })
                        .then(info -> ctx.getResponse().status(Status.OK).send()))
                .delete(() -> {
                    final String oauthId = ctx.getPathTokens().get("oauthid");
                    botOrchestrationService.removeInstallation(oauthId);
                    installationRepository.delete(oauthId);
                    ctx.getResponse().status(Status.OK).send();
                })
        );
    }
}
