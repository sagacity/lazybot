package com.bol.lazybot.config.modules;

import com.bol.lazybot.bot.plugins.PluginDataRepository;
import com.bol.lazybot.bot.plugins.PluginDataRepositoryFactory;
import com.bol.lazybot.bot.plugins.PluginDataRepositoryPersistent;
import com.bol.lazybot.bot.plugins.Plugin;
import com.bol.lazybot.plugins.eod.EodPlugin;
import com.bol.lazybot.plugins.help.HelpPlugin;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;

public class PluginModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginBinder.addBinding().to(HelpPlugin.class);
        pluginBinder.addBinding().to(EodPlugin.class);

        install(new FactoryModuleBuilder()
                .implement(PluginDataRepository.class, PluginDataRepositoryPersistent.class)
                .build(PluginDataRepositoryFactory.class));
    }
}
