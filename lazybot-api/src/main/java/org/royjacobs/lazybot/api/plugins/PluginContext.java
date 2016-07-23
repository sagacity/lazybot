package org.royjacobs.lazybot.api.plugins;

import lombok.Builder;
import lombok.Value;
import org.royjacobs.lazybot.api.hipchat.RoomApi;
import org.royjacobs.lazybot.api.store.Store;

import java.util.Set;

@Value
@Builder
public class PluginContext {
    RoomApi roomApi;
    String roomId;
    Store<? extends PluginRoomData> roomStore;
    Store<? extends PluginGlobalData> globalStore;
    Set<PluginDescriptor> allDescriptors;
}
