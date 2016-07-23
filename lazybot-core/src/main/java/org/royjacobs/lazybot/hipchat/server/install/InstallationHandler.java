package org.royjacobs.lazybot.hipchat.server.install;

import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.hipchat.installations.Installation;
import org.royjacobs.lazybot.hipchat.server.install.dto.InstalledInformation;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.store.StoreFactory;
import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;
import ratpack.http.Status;

import javax.inject.Inject;
import javax.inject.Singleton;

import static ratpack.jackson.Jackson.fromJson;

@Singleton
@Slf4j
public class InstallationHandler extends InjectionHandler {
    private final Store<Installation> installations;

    @Inject
    public InstallationHandler(final StoreFactory storeFactory) throws NoSuitableHandleMethodException {
        installations = storeFactory.get("installations", Installation.class);
    }

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
                            installations.save(installation.getOauthId(), installation);
                            botOrchestrationService.startInstallation(installation);
                        })
                        .then(info -> ctx.getResponse().status(Status.OK).send()))
                .delete(() -> {
                    final String oauthId = ctx.getPathTokens().get("oauthid");
                    botOrchestrationService.removeInstallation(oauthId);
                    installations.delete(oauthId);
                    ctx.getResponse().status(Status.OK).send();
                })
        );
    }
}
