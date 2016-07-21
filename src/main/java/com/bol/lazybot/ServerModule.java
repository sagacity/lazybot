package com.bol.lazybot;

import com.bol.lazybot.server.capabilities.GetCapabilities;
import com.bol.lazybot.server.install.PostInstallation;
import com.google.inject.AbstractModule;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GetCapabilities.class);
        bind(PostInstallation.class);
    }
}
