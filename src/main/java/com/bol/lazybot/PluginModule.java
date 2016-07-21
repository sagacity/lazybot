package com.bol.lazybot;

import com.bol.lazybot.domain.PluginDataRepository;
import com.bol.lazybot.domain.PluginDataRepositoryFactory;
import com.bol.lazybot.domain.PluginDataRepositoryMapDb;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.eod.EodPlugin;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;

public class PluginModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginBinder.addBinding().to(EodPlugin.class);

        install(new FactoryModuleBuilder()
                .implement(PluginDataRepository.class, PluginDataRepositoryMapDb.class)
                .build(PluginDataRepositoryFactory.class));
    }
}
