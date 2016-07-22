package org.royjacobs.lazybot.config.modules;

import org.royjacobs.lazybot.bot.plugins.PluginDataRepository;
import org.royjacobs.lazybot.bot.plugins.PluginDataRepositoryFactory;
import org.royjacobs.lazybot.bot.plugins.PluginDataRepositoryPersistent;
import org.royjacobs.lazybot.bot.plugins.Plugin;
import org.royjacobs.lazybot.plugins.eod.EodPlugin;
import org.royjacobs.lazybot.plugins.help.HelpPlugin;
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
