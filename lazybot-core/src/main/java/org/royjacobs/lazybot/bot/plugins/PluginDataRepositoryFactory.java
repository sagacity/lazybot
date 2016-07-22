package org.royjacobs.lazybot.bot.plugins;

import org.royjacobs.lazybot.hipchat.client.dto.RoomId;

public interface PluginDataRepositoryFactory {
    PluginDataRepository create(final RoomId roomId, final PluginDescriptor pluginDescriptor);
}
