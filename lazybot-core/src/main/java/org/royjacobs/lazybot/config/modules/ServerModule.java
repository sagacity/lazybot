package org.royjacobs.lazybot.config.modules;

import org.royjacobs.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import org.royjacobs.lazybot.hipchat.server.glances.GetGlancesHandler;
import org.royjacobs.lazybot.hipchat.server.glances.GlanceManager;
import org.royjacobs.lazybot.hipchat.server.install.InstallationHandler;
import org.royjacobs.lazybot.hipchat.server.validator.HipChatRequestValidator;
import org.royjacobs.lazybot.hipchat.server.validator.JwtRequestValidator;
import org.royjacobs.lazybot.hipchat.server.webhooks.RoomMessageHandler;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GetCapabilitiesHandler.class);
        bind(InstallationHandler.class);
        bind(RoomMessageHandler.class);
        bind(HipChatRequestValidator.class).to(JwtRequestValidator.class);
        bind(GetGlancesHandler.class);
        bind(GlanceManager.class);
    }
}
