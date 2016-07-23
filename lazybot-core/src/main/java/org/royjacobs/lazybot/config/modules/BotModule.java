package org.royjacobs.lazybot.config.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.royjacobs.lazybot.bot.CommandDispatcher;
import org.royjacobs.lazybot.bot.DefaultCommandDispatcher;
import org.royjacobs.lazybot.bot.plugins.Plugin;
import org.royjacobs.lazybot.plugins.eod.EodPlugin;
import org.royjacobs.lazybot.plugins.help.HelpPlugin;

public class BotModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CommandDispatcher.class).to(DefaultCommandDispatcher.class);

        Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginBinder.addBinding().to(HelpPlugin.class);
        pluginBinder.addBinding().to(EodPlugin.class);
    }
}
