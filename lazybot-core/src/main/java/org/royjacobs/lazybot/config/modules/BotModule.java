package org.royjacobs.lazybot.config.modules;

import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.bot.CommandDispatcher;
import org.royjacobs.lazybot.bot.DefaultCommandDispatcher;

@Slf4j
public class BotModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CommandDispatcher.class).to(DefaultCommandDispatcher.class);
    }
}
