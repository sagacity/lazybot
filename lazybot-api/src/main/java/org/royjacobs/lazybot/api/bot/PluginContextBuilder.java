package org.royjacobs.lazybot.api.bot;

import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;

public interface PluginContextBuilder {
    PluginContext buildContext(final Plugin plugin, final Installation installation);
}
