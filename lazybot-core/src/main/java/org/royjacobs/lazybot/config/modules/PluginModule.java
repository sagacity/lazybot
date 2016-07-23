package org.royjacobs.lazybot.config.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.royjacobs.lazybot.bot.plugins.Plugin;
import org.royjacobs.lazybot.plugins.eod.EodPlugin;
import org.royjacobs.lazybot.plugins.help.HelpPlugin;

public class PluginModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginBinder.addBinding().to(HelpPlugin.class);
        pluginBinder.addBinding().to(EodPlugin.class);
    }
}
