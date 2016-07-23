package org.royjacobs.lazybot.bot.plugins;

import org.royjacobs.lazybot.hipchat.client.RoomApi;
import lombok.Builder;
import lombok.Value;
import org.royjacobs.lazybot.store.Store;

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
