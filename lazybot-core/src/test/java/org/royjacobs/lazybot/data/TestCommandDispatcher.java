package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.bot.CommandDispatcher;

import java.util.*;

public class TestCommandDispatcher implements CommandDispatcher {
    private Map<String, List<RoomMessage>> dispatchedMessages = new HashMap<>();

    @Override
    public void dispatch(final String roomId, Set<Plugin> plugins, RoomMessage message) {
        final List<RoomMessage> list = dispatchedMessages.getOrDefault(roomId, new ArrayList<>());
        list.add(message);
        dispatchedMessages.put(roomId, list);
    }

    public Map<String, List<RoomMessage>> getDispatchedMessages() {
        return dispatchedMessages;
    }
}
