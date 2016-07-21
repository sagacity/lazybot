package com.bol.lazybot.plugins;

public interface Plugin {
    void onStart(final PluginContext context);
    void onStop(final boolean removed);

    PluginDescriptor getDescriptor();
}
