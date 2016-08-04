package org.royjacobs.lazybot.hipchat.server.glances;

import org.royjacobs.lazybot.api.domain.GlanceData;
import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.hipchat.server.validator.HipChatRequestValidator;
import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;

import java.util.Optional;

import static ratpack.jackson.Jackson.json;

public class GetGlancesHandler extends InjectionHandler {
    public void handle(Context ctx, final BotOrchestrationService botOrchestrationService, final HipChatRequestValidator requestValidator, GlanceManager glanceManager) throws Exception {
        final String oauthId = ctx.getPathTokens().get("oauthid");
        botOrchestrationService.getActiveInstallationByOauthId(oauthId).ifPresent(installation -> requestValidator.validate(ctx.getRequest(), installation.getOauthSecret()));

        final String key = ctx.getPathTokens().get("key");
        final Optional<GlanceData> maybeGlance = glanceManager.get(oauthId, key);

        if (maybeGlance.isPresent()) {
            ctx
                    .header("Access-Control-Allow-Origin", "*")
                    .render(json(glanceManager.toContent(maybeGlance.get())));
        } else {
            ctx
                    .header("Access-Control-Allow-Origin", "*")
                    .notFound();
        }
    }
}
