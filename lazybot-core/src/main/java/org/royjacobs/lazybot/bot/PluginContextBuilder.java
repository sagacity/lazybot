package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.hipchat.installations.Installation;

public interface PluginContextBuilder {
    PluginContext buildContext(final Plugin plugin, final Installation installation);
}
