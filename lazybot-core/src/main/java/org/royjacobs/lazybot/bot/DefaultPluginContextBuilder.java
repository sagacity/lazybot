package org.royjacobs.lazybot.bot;

import com.google.inject.assistedinject.Assisted;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.bot.PluginContextBuilder;
import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.api.hipchat.RoomApi;
import org.royjacobs.lazybot.api.plugins.*;
import org.royjacobs.lazybot.api.store.StoreFactory;
import org.royjacobs.lazybot.config.PluginConfig;
import org.royjacobs.lazybot.hipchat.client.RoomApiFactory;
import org.royjacobs.lazybot.utils.JacksonUtils;

import javax.inject.Inject;

@Slf4j
public class DefaultPluginContextBuilder implements PluginContextBuilder {
    private final PluginConfig pluginConfig;
    private final RoomApiFactory roomApiFactory;
    private final StoreFactory storeFactory;
    private final VariableCombiner variableCombiner;

    @Inject
    public DefaultPluginContextBuilder(
            PluginConfig pluginConfig,
            RoomApiFactory roomApiFactory,
            StoreFactory storeFactory,
            @Assisted VariableCombiner variableCombiner
    ) {
        this.pluginConfig = pluginConfig;
        this.roomApiFactory = roomApiFactory;
        this.storeFactory = storeFactory;
        this.variableCombiner = variableCombiner;
    }

    @Override
    public PluginContext buildContext(final Plugin plugin, final Installation installation) {
        final RoomApi roomApi = roomApiFactory.create(installation);

        final PluginContext.PluginContextBuilder pluginContextBuilder = PluginContext.builder()
                .roomApi(roomApi)
                .roomId(installation.getRoomId());

        // If the plugin requires configuration data, we'll grab it from our main pluginConfig object and try to deserialize it into the requested format
        final Class<? extends PluginConfigData> configDataClass = plugin.getDescriptor().getConfigDataClass();

        final Object pluginConfigurationData = pluginConfig.getParameters().get(plugin.getDescriptor().getKey());
        if (configDataClass != null && pluginConfigurationData != null) {
            final String serialized = JacksonUtils.serialize(pluginConfigurationData);
            final PluginConfigData deserialized = JacksonUtils.deserialize(serialized, configDataClass);
            pluginContextBuilder.configData(deserialized);
        }

        if (plugin.getDescriptor().getRoomDataClass() != null) {
            pluginContextBuilder.roomStore(storeFactory.get("plugindata-" + plugin.getDescriptor().getKey() + "-room-" + installation.getRoomId(), (Class<PluginRoomData>)plugin.getDescriptor().getRoomDataClass()));
        }

        if (plugin.getDescriptor().getGlobalDataClass() != null) {
            pluginContextBuilder.globalStore(storeFactory.get("plugindata-" + plugin.getDescriptor().getKey() + "-global", (Class<PluginGlobalData>)plugin.getDescriptor().getGlobalDataClass()));
        }

        pluginContextBuilder.publicVariables(variableCombiner.getCurrentVariables());

        return pluginContextBuilder.build();
    }
}
