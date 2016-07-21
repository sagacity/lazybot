package org.royjacobs.lazybot.bot.plugins;

import org.royjacobs.lazybot.hipchat.client.RoomId;

public interface PluginDataRepositoryFactory {
    PluginDataRepository create(final RoomId roomId, final PluginDescriptor pluginDescriptor);
}
