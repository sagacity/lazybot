package org.royjacobs.lazybot.config.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.royjacobs.lazybot.api.plugins.Plugin;

import java.util.Set;

@Slf4j
public class PluginModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginBinder = Multibinder.newSetBinder(binder(), Plugin.class);
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.royjacobs.lazybot"))
        );
        Set<Class<? extends Plugin>> pluginTypes = reflections.getSubTypesOf(Plugin.class);
        for (Class<? extends Plugin> pluginType : pluginTypes) {
            log.info("Registering plugin: " + pluginType.getSimpleName());
            pluginBinder.addBinding().to(pluginType);
        }
    }
}
