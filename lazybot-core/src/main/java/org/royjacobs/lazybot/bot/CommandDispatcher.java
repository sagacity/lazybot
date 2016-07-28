package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;

import java.util.Set;

public interface CommandDispatcher {
    PluginMessageHandlingResult dispatch(final String roomId, final Set<Plugin> plugins, final Command command);
}
