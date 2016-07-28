package org.royjacobs.lazybot.api.plugins;

import org.royjacobs.lazybot.api.domain.Command;

public interface Plugin {
    PluginDescriptor getDescriptor();

    void onStart(final PluginContext context);
    void onStop(final boolean unregistered);

    PluginMessageHandlingResult onCommand(final Command command);
    default PluginMessageHandlingResult onUnhandledCommand(final Command command) {
        return PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
    }
}
