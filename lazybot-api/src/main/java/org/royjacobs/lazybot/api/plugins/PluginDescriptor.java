package org.royjacobs.lazybot.api.plugins;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PluginDescriptor {
    private String key;
    private Class<? extends PluginConfigData> configDataClass;
    private Class<? extends PluginRoomData> roomDataClass;
    private Class<? extends PluginGlobalData> globalDataClass;
}
