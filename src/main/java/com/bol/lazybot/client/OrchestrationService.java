package com.bol.lazybot.client;

import com.bol.lazybot.domain.Installation;
import com.bol.lazybot.domain.InstallationRepository;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.PluginContext;
import lombok.extern.slf4j.Slf4j;
import ratpack.service.Service;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class OrchestrationService implements Service {
    private final InstallationRepository installationRepository;
    private final Provider<Set<Plugin>> pluginProvider;
    private final OAuthApi oAuthApi;
    private final RoomApiFactory roomApiFactory;

    private final Map<Installation, Set<Plugin>> activePlugins;

    @Inject
    public OrchestrationService(final InstallationRepository installationRepository, final Provider<Set<Plugin>> pluginProvider, final OAuthApi oAuthApi, final RoomApiFactory roomApiFactory) {
        this.installationRepository = installationRepository;
        this.pluginProvider = pluginProvider;
        this.oAuthApi = oAuthApi;
        this.roomApiFactory = roomApiFactory;
        this.activePlugins = new HashMap<>();
    }

    public void onStart(final StartEvent event) throws IOException {
        for (Installation installation : installationRepository.findAll()) {
            log.info("Starting installation: " + installation);

            final Set<Plugin> plugins = new HashSet<>();

            final PluginContext context = PluginContext.builder()
                    .roomApi(getRoomApi(installation))
                    .roomId(installation.getRoomId())
                    .build();

            pluginProvider.get().forEach(plugin -> {
                plugin.onStart(context);
                plugins.add(plugin);
            });

            activePlugins.put(installation, plugins);
        }
    }

    public void onStop(final StopEvent event) {
        for (Installation installation : activePlugins.keySet()) {
            activePlugins.get(installation).forEach(Plugin::onStop);
        }
    }

    private RoomApi getRoomApi(final Installation installation) throws IOException {
        final RequestTokenResponse response = oAuthApi.requestToken(installation.getOauthId(), installation.getOauthSecret());
        return roomApiFactory.create(new RoomId(installation.getRoomId()), new OAuthToken(response.getAccessToken()));
    }
}
