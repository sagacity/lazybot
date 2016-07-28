package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.plugins.Plugin;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestPluginProvider implements Provider<Set<Plugin>> {
    private final List<Provider<TestPlugin>> pluginProviders;

    private final List<TestPlugin> createdPlugins = new ArrayList<>();

    @SafeVarargs
    public TestPluginProvider(Provider<TestPlugin>... pluginProviders) {
        this.pluginProviders = Arrays.asList(pluginProviders);
    }

    @Override
    public Set<Plugin> get() {
        return pluginProviders.stream()
                .map(pv -> {
                    final TestPlugin plugin = pv.get();
                    createdPlugins.add(plugin);
                    return plugin;
                })
                .collect(Collectors.toSet());
    }

    public List<TestPlugin> getCreatedPlugins() {
        return createdPlugins;
    }
}

