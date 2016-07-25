package org.royjacobs.lazybot.api.plugins;

import lombok.Builder;
import lombok.Value;
import rx.Observable;

@Value
@Builder
public class PluginDescriptor {
    private String key;
    private Class<? extends PluginConfigData> configDataClass;
    private Class<? extends PluginRoomData> roomDataClass;
    private Class<? extends PluginGlobalData> globalDataClass;
    private Observable<PublicVariables> publicVariables;
}
