package org.royjacobs.lazybot.config.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.api.bot.CommandDispatcher;
import org.royjacobs.lazybot.bot.DefaultCommandDispatcher;
import org.royjacobs.lazybot.bot.DefaultPluginContextBuilder;
import org.royjacobs.lazybot.api.bot.PluginContextBuilder;
import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.api.store.StoreFactory;

@Slf4j
public class BotModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CommandDispatcher.class).to(DefaultCommandDispatcher.class);
        bind(PluginContextBuilder.class).to(DefaultPluginContextBuilder.class);
    }

    @Provides
    Store<Installation> provideInstallationStore(StoreFactory storeFactory) {
        return storeFactory.get("installations", Installation.class);
    }
}
