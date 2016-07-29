package org.royjacobs.lazybot.testing;

import org.junit.Assert;
import org.royjacobs.lazybot.api.bot.PluginContextBuilder;
import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import rx.subjects.PublishSubject;

import java.util.HashMap;
import java.util.Map;

public class TestPluginContextBuilder implements PluginContextBuilder {
    private final Map<Plugin, PluginContext> createdContexts = new HashMap<>();

    @Override
    public PluginContext buildContext(Plugin plugin, Installation installation) {
        final PluginContext context = PluginContext.builder()
                .roomApi(new TestRoomApi())
                .roomId(installation.getRoomId())
                .roomStore(new TestStore<>())
                .publicVariables(PublishSubject.create())
                .build();
        createdContexts.put(plugin, context);
        return context;
    }

    public PluginContext getContextFor(Plugin plugin) {
        final PluginContext context = createdContexts.get(plugin);
        if (context == null) Assert.fail("Context not found for plugin: " + plugin);
        return context;
    }
}
