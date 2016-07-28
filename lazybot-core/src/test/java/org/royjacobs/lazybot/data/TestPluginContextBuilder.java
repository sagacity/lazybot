package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.bot.PluginContextBuilder;
import org.royjacobs.lazybot.hipchat.installations.Installation;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class TestPluginContextBuilder implements PluginContextBuilder {
    private final Map<Plugin, PluginContext> createdContexts = new HashMap<>();

    @Override
    public PluginContext buildContext(Plugin plugin, Installation installation) {
        final PluginContext context = PluginContext.builder()
                .roomId(installation.getRoomId())
                .roomStore(new TestStore<>())
                .build();
        createdContexts.put(plugin, context);
        return context;
    }

    public PluginContext getContextFor(Plugin plugin) {
        final PluginContext context = createdContexts.get(plugin);
        if (context == null) fail("Context not found for plugin: " + plugin);
        return context;
    }
}
