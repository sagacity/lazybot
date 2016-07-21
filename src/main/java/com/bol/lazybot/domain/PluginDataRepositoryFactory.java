package com.bol.lazybot.domain;

import com.bol.lazybot.client.RoomId;
import com.bol.lazybot.plugins.PluginDescriptor;

public interface PluginDataRepositoryFactory {
    PluginDataRepository create(final RoomId roomId, final PluginDescriptor pluginDescriptor);
}
