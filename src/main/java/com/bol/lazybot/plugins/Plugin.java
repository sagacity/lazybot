package com.bol.lazybot.plugins;

import com.bol.lazybot.bot.Command;

public interface Plugin {
    PluginDescriptor getDescriptor();

    void onStart(final PluginContext context);
    void onStop(final boolean removed);

    PluginMessageHandlingResult onCommand(final Command command);
    default PluginMessageHandlingResult onUnhandledCommand(final Command command) {
        return PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
    }
}
