package org.royjacobs.lazybot.config.modules;

import org.royjacobs.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import org.royjacobs.lazybot.hipchat.server.install.InstallationHandler;
import org.royjacobs.lazybot.hipchat.server.webhooks.RoomMessageHandler;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GetCapabilitiesHandler.class);
        bind(InstallationHandler.class);
        bind(RoomMessageHandler.class);
    }
}
