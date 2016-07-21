package com.bol.lazybot.plugins;

import com.bol.lazybot.client.RoomApi;
import com.bol.lazybot.domain.PluginDataRepository;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PluginContext {
    RoomApi roomApi;
    String roomId;
    PluginDataRepository repository;
}
