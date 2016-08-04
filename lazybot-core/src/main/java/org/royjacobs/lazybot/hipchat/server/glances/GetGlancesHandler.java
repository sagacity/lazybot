package org.royjacobs.lazybot.hipchat.server.glances;

import org.royjacobs.lazybot.api.domain.GlanceData;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.Optional;

import static ratpack.jackson.Jackson.json;

@Singleton
public class GetGlancesHandler implements Handler {
    private final GlanceManager glanceManager;

    @Inject
    public GetGlancesHandler(GlanceManager glanceManager) {
        this.glanceManager = glanceManager;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        final String roomId = ctx.getPathTokens().get("roomid");
        final String key = ctx.getPathTokens().get("key");
        final Optional<GlanceData> maybeGlance = glanceManager.get(roomId, key);

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
