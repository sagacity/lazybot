package com.bol.lazybot.config.modules;

import com.bol.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import com.bol.lazybot.hipchat.server.install.InstallationHandler;
import com.bol.lazybot.hipchat.server.webhooks.RoomMessageHandler;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GetCapabilitiesHandler.class);
        bind(InstallationHandler.class);
        bind(RoomMessageHandler.class);
    }
}
