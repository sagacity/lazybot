package com.bol.lazybot.client;

import com.bol.lazybot.domain.*;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.PluginContext;
import lombok.extern.slf4j.Slf4j;
import ratpack.service.Service;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Singleton
public class OrchestrationService implements Service {
    private final InstallationRepository installationRepository;
    private final Provider<Set<Plugin>> pluginProvider;
    private final OAuthApi oAuthApi;
    private final RoomApiFactory roomApiFactory;
    private final PluginDataRepositoryFactory pluginDataRepositoryFactory;

    private final Map<String, Set<Plugin>> activeInstallations;
    private final Map<Plugin, PluginContext> activeContexts;

    @Inject
    public OrchestrationService(
            final InstallationRepository installationRepository,
            final Provider<Set<Plugin>> pluginProvider,
            final OAuthApi oAuthApi,
            final RoomApiFactory roomApiFactory,
            final PluginDataRepositoryFactory pluginDataRepositoryFactory
    ) {
        this.installationRepository = installationRepository;
        this.pluginProvider = pluginProvider;
        this.oAuthApi = oAuthApi;
        this.roomApiFactory = roomApiFactory;
        this.pluginDataRepositoryFactory = pluginDataRepositoryFactory;
        this.activeInstallations = new HashMap<>();
        this.activeContexts = new HashMap<>();
    }

    public void onStart(final StartEvent event) throws IOException {
        for (Installation installation : installationRepository.findAll()) {
            try {
                startInstallation(installation);
            } catch (Exception e) {
                log.error("Could not start installation for room: " + installation.getRoomId(), e);
            }
        }
    }

    public void startInstallation(final Installation installation) throws IOException {
        log.info("Starting installation: " + installation);

        final Set<Plugin> plugins = new HashSet<>();

        final RoomApi roomApi = getRoomApi(installation);
        pluginProvider.get().forEach(plugin -> {
            final PluginContext context = PluginContext.builder()
                    .roomApi(roomApi)
                    .roomId(installation.getRoomId())
                    .repository(pluginDataRepositoryFactory.create(new RoomId(installation.getRoomId()), plugin.getDescriptor()))
                    .build();

            activeContexts.put(plugin, context);

            plugin.onStart(context);
            plugins.add(plugin);
        });

        activeInstallations.put(installation.getOauthId(), plugins);
    }

    public void onStop(final StopEvent event) {
        for (final String oauthId: activeInstallations.keySet()) {
            activeInstallations.get(oauthId).forEach(plugin -> {
                // Notify plugin
                plugin.onStop(false);
            });
        }
    }

    private RoomApi getRoomApi(final Installation installation) throws IOException {
        final RequestTokenResponse response = oAuthApi.requestToken(installation.getOauthId(), installation.getOauthSecret());
        return roomApiFactory.create(new RoomId(installation.getRoomId()), new OAuthToken(response.getAccessToken()));
    }

    public void removeInstallation(final String oauthId) {
        final Set<Plugin> plugins = activeInstallations.get(oauthId);
        if (plugins == null) return; // already removed due to some error

        for (Plugin plugin : plugins) {
            // Remove context and clear any data
            final PluginContext context = activeContexts.get(plugin);
            context.getRepository().clearAll();
            activeContexts.remove(plugin);

            // Notify plugin
            plugin.onStop(true);
        }
    }
}
