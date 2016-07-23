package org.royjacobs.lazybot.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.*;
import org.royjacobs.lazybot.api.hipchat.RoomApi;
import org.royjacobs.lazybot.config.PluginConfig;
import org.royjacobs.lazybot.hipchat.client.RoomApiFactory;
import org.royjacobs.lazybot.hipchat.installations.Installation;
import org.royjacobs.lazybot.hipchat.installations.InstallationContext;
import org.royjacobs.lazybot.hipchat.installations.InstalledPlugin;
import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.store.StoreFactory;
import org.royjacobs.lazybot.utils.JacksonUtils;
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
    private final PluginConfig pluginConfig;
    private final StoreFactory storeFactory;
    private final Provider<Set<Plugin>> pluginProvider;
    private final RoomApiFactory roomApiFactory;
    private final CommandDispatcher commandDispatcher;
    private final ObjectMapper objectMapper;

    private final Map<Installation, InstallationContext> activeInstallations;
    private final Store<Installation> storedInstallations;

    @Inject
    public BotOrchestrationService(
            final PluginConfig pluginConfig,
            final StoreFactory storeFactory,
            final Provider<Set<Plugin>> pluginProvider,
            final RoomApiFactory roomApiFactory,
            final CommandDispatcher commandDispatcher,
            final ObjectMapper objectMapper
            ) {
        this.pluginConfig = pluginConfig;
        this.storeFactory = storeFactory;
        this.pluginProvider = pluginProvider;
        this.roomApiFactory = roomApiFactory;
        this.commandDispatcher = commandDispatcher;
        this.objectMapper = objectMapper;
        this.activeInstallations = new HashMap<>();

        storedInstallations = storeFactory.get("installations", Installation.class);
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

    public void startInstallation(final Installation installation) throws IOException {
        log.info("Starting installation: " + installation);

        final InstallationContext.InstallationContextBuilder builder = InstallationContext
                .builder()
                .installation(installation);

        log.info("Creating roomApi");
        final RoomApi roomApi = roomApiFactory.create(installation);

        log.info("Creating plugins");
        Set<PluginDescriptor> allDescriptors = new HashSet<>();

        pluginProvider.get().forEach(plugin -> {
            allDescriptors.add(plugin.getDescriptor());

            final PluginContext.PluginContextBuilder pluginContextBuilder = PluginContext.builder()
                    .roomApi(roomApi)
                    .roomId(installation.getRoomId())
                    .allDescriptors(allDescriptors); // because this list is mutable, it will eventually contain descriptors for all the plugins

            // If the plugin requires configuration data, we'll grab it from our main pluginConfig object and try to deserialize it into the requested format
            final Class<? extends PluginConfigData> configDataClass = plugin.getDescriptor().getConfigDataClass();
            final Object pluginConfigurationData = pluginConfig.getParameters().get(plugin.getDescriptor().getKey());
            if (configDataClass != null && pluginConfigurationData != null) {
                final String serialized = JacksonUtils.serialize(objectMapper, pluginConfigurationData);
                final PluginConfigData deserialized = JacksonUtils.deserialize(objectMapper, serialized, configDataClass);
                pluginContextBuilder.configData(deserialized);
            }

            if (plugin.getDescriptor().getRoomDataClass() != null) {
                pluginContextBuilder.roomStore(storeFactory.get("plugindata-" + plugin.getDescriptor().getKey() + "-room-" + installation.getRoomId(), plugin.getDescriptor().getRoomDataClass()));
            }

            if (plugin.getDescriptor().getGlobalDataClass() != null) {
                pluginContextBuilder.globalStore(storeFactory.get("plugindata-" + plugin.getDescriptor().getKey() + "-global", plugin.getDescriptor().getGlobalDataClass()));
            }

            final PluginContext pluginContext = pluginContextBuilder.build();
            final InstalledPlugin installedPlugin = InstalledPlugin.builder()
                    .plugin(plugin)
                    .context(pluginContext)
                    .build();
            builder.plugin(installedPlugin);

            plugin.onStart(pluginContext);
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
    }

    public void removeInstallation(final String oauthId) {
        getInstallationByOauthId(oauthId).ifPresent(context -> {
            for (InstalledPlugin plugin : context.getPlugins()) {
                // Remove context and clear any data
                plugin.getContext().getRoomStore().clearAll();

                // Notify plugin
                plugin.getPlugin().onStop(true);
            }

            removeInstallationByOauthId(oauthId);
        });
    }

    public void onRoomMessage(final RoomMessage message) {
        getInstallationByOauthId(message.getOauthId()).ifPresent(context -> {
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

    public Optional<InstallationContext> getInstallationByOauthId(String oauthId) {
        return activeInstallations.entrySet().stream().filter(kv -> Objects.equals(kv.getKey().getOauthId(), oauthId)).map(Map.Entry::getValue).findFirst();
    }

    private void removeInstallationByOauthId(String oauthId) {
        activeInstallations.entrySet().stream().filter(kv -> Objects.equals(kv.getKey().getOauthId(), oauthId)).map(Map.Entry::getKey).findFirst().ifPresent(activeInstallations::remove);
    }
}
