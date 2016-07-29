package org.royjacobs.lazybot.bot;

import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.bot.CommandDispatcher;
import org.royjacobs.lazybot.api.bot.PluginContextBuilder;
import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.hipchat.installations.InstallationContext;
import org.royjacobs.lazybot.hipchat.installations.InstalledPlugin;
import ratpack.service.Service;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class BotOrchestrationService implements Service {
    private final PluginContextBuilder pluginContextBuilder;
    private final Provider<Set<Plugin>> pluginProvider;
    private final CommandDispatcher commandDispatcher;

    private final Map<Installation, InstallationContext> activeInstallations;
    private final Store<Installation> storedInstallations;

    @Inject
    public BotOrchestrationService(
            final PluginContextBuilder pluginContextBuilder,
            final Store<Installation> storedInstallations,
            final Provider<Set<Plugin>> pluginProvider,
            final CommandDispatcher commandDispatcher
            ) {
        this.pluginContextBuilder = pluginContextBuilder;
        this.storedInstallations = storedInstallations;
        this.pluginProvider = pluginProvider;
        this.commandDispatcher = commandDispatcher;

        activeInstallations = new HashMap<>();
    }

    public void onStart(final StartEvent event) {
        storedInstallations.findAll().forEach(this::startInstallation);
    }

    public void registerInstallation(final Installation installation) {
        storedInstallations.save(installation.getOauthId(), installation);
        startInstallation(installation);
    }

    private void startInstallation(final Installation installation) {
        if (activeInstallations.containsKey(installation)) {
            log.info("Skipping installation (was already started): " + installation);
            return;
        }

        log.info("Starting installation: " + installation);

        final InstallationContext.InstallationContextBuilder builder = InstallationContext
                .builder();

        pluginProvider.get().forEach(plugin -> {
            try {
                final PluginContext pluginContext = pluginContextBuilder.buildContext(plugin, installation);
                plugin.onStart(pluginContext);

                final InstalledPlugin installedPlugin = InstalledPlugin.builder()
                        .plugin(plugin)
                        .context(pluginContext)
                        .build();
                builder.plugin(installedPlugin);
            } catch (Exception e) {
                log.warn("Could not add plugin '" + plugin.getDescriptor().getKey() + "' to installation", e);
            }
        });

        log.info("Installation complete");
        activeInstallations.put(installation, builder.build());
    }

    public void onStop(final StopEvent event) {
        for (InstallationContext context : activeInstallations.values()) {
            for (InstalledPlugin plugin: context.getPlugins()) {
                // Notify plugin
                plugin.getPlugin().onStop(false);
            }
        }

        activeInstallations.clear();
    }

    public void unregisterInstallation(final Installation installation) {
        final InstallationContext context = getContext(installation);

        if (context != null) { // not stopped yet
            for (InstalledPlugin plugin : context.getPlugins()) {
                // Remove context and clear any data
                plugin.getContext().getRoomStore().clearAll();

                // Notify plugin
                plugin.getPlugin().onStop(true);
            }

            activeInstallations.remove(installation);
        }

        storedInstallations.delete(installation.getOauthId());
    }

    public void onRoomMessage(final RoomMessage message) {
        getActiveInstallationByOauthId(message.getOauthId()).ifPresent(installation -> {
            final InstallationContext context = getContext(installation);
            commandDispatcher.dispatch(installation.getRoomId(), context.getPlugins().stream().map(InstalledPlugin::getPlugin).collect(Collectors.toSet()), message);
        });
    }

    public Optional<Installation> getActiveInstallationByOauthId(String oauthId) {
        return activeInstallations.keySet().stream().filter(i -> i.getOauthId().equals(oauthId)).findFirst();
    }

    public InstallationContext getContext(Installation installation) {
        return activeInstallations.get(installation);
    }
}
