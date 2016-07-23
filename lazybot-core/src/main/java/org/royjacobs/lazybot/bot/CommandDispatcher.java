package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.bot.plugins.Plugin;
import org.royjacobs.lazybot.bot.plugins.PluginMessageHandlingResult;

import java.util.Set;

public interface CommandDispatcher {
    PluginMessageHandlingResult dispatch(final Set<Plugin> plugins, final Command command);
}
