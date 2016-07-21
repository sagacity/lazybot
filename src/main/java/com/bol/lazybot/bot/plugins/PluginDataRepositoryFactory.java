package com.bol.lazybot.bot.plugins;

import com.bol.lazybot.hipchat.client.RoomId;

public interface PluginDataRepositoryFactory {
    PluginDataRepository create(final RoomId roomId, final PluginDescriptor pluginDescriptor);
}
