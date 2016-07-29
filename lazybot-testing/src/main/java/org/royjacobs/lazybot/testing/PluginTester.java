package org.royjacobs.lazybot.testing;

import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.api.plugins.PublicVariables;
import rx.subjects.PublishSubject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PluginTester<T extends Plugin> {
    private final T plugin;
    private final PluginContext pluginContext;

    public PluginTester(final Supplier<T> pluginSupplier) {
        plugin = pluginSupplier.get();

        final Installation installation = Installation.builder()
                .roomId("roomId")
                .build();

        final TestPluginContextBuilder contextBuilder = new TestPluginContextBuilder();
        pluginContext = contextBuilder.buildContext(plugin, installation);
        plugin.onStart(pluginContext);
    }

    public void test(final Consumer<T> fn) {
        fn.accept(plugin);
    }

    public TestRoomApi getRoomApi() {
        return (TestRoomApi)pluginContext.getRoomApi();
    }

    public PublishSubject<PublicVariables> getPublicVariables() {
        return (PublishSubject<PublicVariables>)pluginContext.getPublicVariables();
    }
}
