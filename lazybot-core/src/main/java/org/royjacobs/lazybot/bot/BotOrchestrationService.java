package org.royjacobs.lazybot.bot;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;
import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.hipchat.installations.Installation;
import org.royjacobs.lazybot.hipchat.installations.InstallationContext;
import org.royjacobs.lazybot.hipchat.installations.InstalledPlugin;
import ratpack.service.Service;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
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

    public void onStart(final StartEvent event) throws IOException {
        for (Installation installation : storedInstallations.findAll()) {
            try {
                startInstallation(installation);
            } catch (Exception e) {
                log.error("Could not start installation for room: " + installation.getRoomId(), e);
            }
        }
    }

    public void registerInstallation(final Installation installation) throws IOException {
        storedInstallations.save(installation.getOauthId(), installation);
        startInstallation(installation);
    }

    private void startInstallation(final Installation installation) throws IOException {
        if (activeInstallations.containsKey(installation)) {
            log.info("Skipping installation (was already started): " + installation);
            return;
        }

        log.info("Starting installation: " + installation);

        final InstallationContext.InstallationContextBuilder builder = InstallationContext
                .builder();

        pluginProvider.get().forEach(plugin -> {
            final PluginContext pluginContext = pluginContextBuilder.buildContext(plugin, installation);
            plugin.onStart(pluginContext);

            final InstalledPlugin installedPlugin = InstalledPlugin.builder()
                    .plugin(plugin)
                    .context(pluginContext)
                    .build();
            builder.plugin(installedPlugin);
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

            final List<String> cmdLine = Splitter.on(CharMatcher.WHITESPACE)
                    .trimResults()
                    .omitEmptyStrings()
                    .splitToList(message.getItem().getMessage().getMessage());
            if (cmdLine.isEmpty()) return;
            if (!cmdLine.get(0).equalsIgnoreCase("/lazybot")) return;

            final Command command = Command.builder()
                    .originalMessage(message)
                    .command(cmdLine.size() > 1 ? cmdLine.get(1) : "help")
                    .args(cmdLine.size() > 2 ? cmdLine.subList(2, cmdLine.size()) : Collections.emptyList())
                    .build();

            final PluginMessageHandlingResult result = commandDispatcher.dispatch(context.getPlugins().stream().map(InstalledPlugin::getPlugin).collect(Collectors.toSet()), command);
            switch (result) {
                case BAD_REQUEST:
                case FAILURE:
                    log.error("Could not handle message (result: " + result + "): {}", message);
            }
        });
    }

    public Optional<Installation> getActiveInstallationByOauthId(String oauthId) {
        return activeInstallations.keySet().stream().filter(i -> i.getOauthId().equals(oauthId)).findFirst();
    }

    public InstallationContext getContext(Installation installation) {
        return activeInstallations.get(installation);
    }
}
