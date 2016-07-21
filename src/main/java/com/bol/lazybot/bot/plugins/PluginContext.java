package com.bol.lazybot.bot.plugins;

import com.bol.lazybot.hipchat.client.RoomApi;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class PluginContext {
    RoomApi roomApi;
    String roomId;
    PluginDataRepository repository;
    Set<PluginDescriptor> allDescriptors;
}
