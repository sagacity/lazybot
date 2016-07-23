package org.royjacobs.lazybot.hipchat.server.webhooks;

import com.google.common.base.Throwables;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.hipchat.installations.InstallationContext;
import org.royjacobs.lazybot.api.domain.RoomMessage;
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
                        .next(msg -> botOrchestrationService.getInstallationByOauthId(msg.getOauthId()).ifPresent(installation -> validateRequest(ctx, installation)))
                        .next(botOrchestrationService::onRoomMessage)
                        .then(msg -> ctx.getResponse().status(Status.OK).send())
                )
        );
    }

    private void validateRequest(final Context ctx, final InstallationContext installationContext) {
        String jwtToken = ctx.getRequest().getQueryParams().get("signed_request");
        if (jwtToken == null) {
            final String headerValue = ctx.getRequest().getHeaders().get("Authorization");
            if (headerValue != null) jwtToken = headerValue.substring(4); // remove "JWT " from header value
        }
        if (jwtToken == null) throw new UnsupportedOperationException("Could not retrieve JWT token");

        try {
            Jwts.parser()
                    .setSigningKey(installationContext.getInstallation().getOauthSecret().getBytes())
                    .parse(jwtToken);
        } catch (SignatureException e) {
            log.warn("Could not validate JWT token. Ignoring message.", e);
            throw Throwables.propagate(e);
        }
    }
}
