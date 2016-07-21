package com.bol.lazybot.server.install;

import com.bol.lazybot.domain.Installation;
import com.bol.lazybot.domain.InstallationRepository;
import com.bol.lazybot.server.install.dto.InstalledInformation;
import lombok.extern.slf4j.Slf4j;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.handling.InjectionHandler;
import ratpack.http.Status;

import javax.inject.Singleton;

import static ratpack.jackson.Jackson.fromJson;

@Singleton
@Slf4j
public class PostInstallation extends InjectionHandler {
    public void handle(final Context ctx, final InstallationRepository installationRepository) throws Exception {
        ctx.byMethod(m -> m
                .post(() -> {
                    ctx.parse(fromJson(InstalledInformation.class))
                            .next(info -> {
                                log.debug("Received installation request: {}", info);
                                final Installation installation = Installation.builder()
                                        .oauthId(info.getOauthId())
                                        .oauthSecret(info.getOauthSecret())
                                        .roomId(info.getRoomId())
                                        .build();
                                installationRepository.save(installation);
                            })
                            .then(info -> ctx.getResponse().status(Status.OK).send());
                })
                .delete(() -> {
                    final String oauthId = ctx.getPathTokens().get("oauth-id");
                    installationRepository.delete(oauthId);
                })
        );
    }
}
