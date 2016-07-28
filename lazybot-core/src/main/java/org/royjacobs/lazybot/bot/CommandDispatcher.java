package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.plugins.Plugin;

import java.util.Set;

public interface CommandDispatcher {
    void dispatch(final String roomId, final Set<Plugin> plugins, final RoomMessage message);
}
