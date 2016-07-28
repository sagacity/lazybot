package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.plugins.PluginContext;

public class CannotStartPlugin extends TestPlugin {
    public CannotStartPlugin() {
    }

    public CannotStartPlugin(String key) {
        super(key);
    }

    @Override
    public void onStart(PluginContext context) {
        throw new UnsupportedOperationException();
    }
}
