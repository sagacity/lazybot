package com.bol.lazybot;

import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.eod.EodPlugin;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class PluginModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginBinder.addBinding().to(EodPlugin.class);
    }
}
