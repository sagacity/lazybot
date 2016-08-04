package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.api.bot.PluginContextBuilder;

public interface PluginContextBuilderFactory {
    PluginContextBuilder create(final VariableCombiner variableCombiner);
}
