package com.bol.lazybot.server.installed;

import com.bol.lazybot.server.installed.dto.InstalledInformation;
import lombok.extern.slf4j.Slf4j;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Status;

import javax.inject.Singleton;

import static ratpack.jackson.Jackson.fromJson;

@Singleton
@Slf4j
public class InstalledHandler implements Handler {
    @Override
    public void handle(Context context) throws Exception {
        context.byMethod(m -> m
                .post(() -> {
                    context.parse(fromJson(InstalledInformation.class))
                            .next(info -> {
                                log.info("Received installation request: {}", info);
                            })
                            .then(info -> context.getResponse().status(Status.OK).send());
                })
                .delete(() -> {
                    // TODO, url contains oauth id to remove
                })
        );
    }
}
