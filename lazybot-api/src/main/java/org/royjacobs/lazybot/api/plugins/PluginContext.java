package org.royjacobs.lazybot.api.plugins;

import lombok.Builder;
import lombok.Value;
import org.royjacobs.lazybot.api.hipchat.RoomApi;
import org.royjacobs.lazybot.api.store.Store;
import rx.Observable;

@Value
@Builder
public class PluginContext {
    RoomApi roomApi;
    String roomId;
    PluginConfigData configData;
    Store<PluginRoomData> roomStore;
    Store<PluginGlobalData> globalStore;
    //Set<PluginDescriptor> allDescriptors;
    Observable<PublicVariables> publicVariables;
}
