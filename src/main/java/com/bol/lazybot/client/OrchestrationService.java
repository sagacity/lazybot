package com.bol.lazybot.client;

import com.bol.lazybot.domain.Installation;
import com.bol.lazybot.domain.InstallationRepository;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.PluginContext;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import ratpack.service.Service;
import ratpack.service.StartEvent;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Set;

@Slf4j
public class OrchestrationService implements Service {
    private final InstallationRepository installationRepository;
    private final Provider<Set<Plugin>> pluginProvider;

    @Inject
    public OrchestrationService(final InstallationRepository installationRepository, final Provider<Set<Plugin>> pluginProvider) {
        this.installationRepository = installationRepository;
        this.pluginProvider = pluginProvider;
    }

    public void onStart(final StartEvent event) {
        for (Installation installation : installationRepository.findAll()) {
            log.info("Starting installation: " + installation);

            final PluginContext context = PluginContext.builder()
                    .roomApi(getRoomApi(installation))
                    .roomId(installation.getRoomId())
                    .build();

            pluginProvider.get().forEach(plugin -> plugin.onStart(context));
        }
    }

    private RoomApi getRoomApi(final Installation installation) {
        final HipChatOAuth oauthApi = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(installation.getOauthId(), installation.getOauthSecret()))
                .target(HipChatOAuth.class, "https://api.hipchat.com");

        final RequestTokenResponse response = oauthApi.requestToken("send_notification+view_messages+view_room");

        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .requestInterceptor(new AuthInterceptor(response.getAccessToken()))
                .target(RoomApi.class, "https://api.hipchat.com");
    }
}
