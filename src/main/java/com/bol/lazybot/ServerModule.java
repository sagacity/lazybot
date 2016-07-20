package com.bol.lazybot;

import com.bol.lazybot.server.capability.CapabilityHandler;
import com.bol.lazybot.server.installed.InstalledHandler;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CapabilityHandler.class);
        bind(InstalledHandler.class);
    }
}
