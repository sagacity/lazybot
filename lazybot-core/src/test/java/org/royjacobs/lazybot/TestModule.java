package org.royjacobs.lazybot;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.data.TestPlugin;

public class TestModule extends AbstractModule {
    @Override
    public void configure() {
        Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginBinder.addBinding().toInstance(new TestPlugin("foo"));
        pluginBinder.addBinding().toInstance(new TestPlugin("bar"));
    }
}
