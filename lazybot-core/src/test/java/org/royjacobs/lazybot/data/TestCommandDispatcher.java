package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;
import org.royjacobs.lazybot.bot.CommandDispatcher;

import java.util.*;

import static org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult.SUCCESS;

public class TestCommandDispatcher implements CommandDispatcher {
    private Map<String, List<Command>> dispatchedCommands = new HashMap<>();

    @Override
    public PluginMessageHandlingResult dispatch(final String roomId, Set<Plugin> plugins, Command command) {
        final List<Command> list = dispatchedCommands.getOrDefault(roomId, new ArrayList<>());
        list.add(command);
        dispatchedCommands.put(roomId, list);
        return SUCCESS;
    }

    public Map<String, List<Command>> getDispatchedCommands() {
        return dispatchedCommands;
    }
}
