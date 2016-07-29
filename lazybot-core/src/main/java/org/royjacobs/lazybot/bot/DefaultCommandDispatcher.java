package org.royjacobs.lazybot.bot;

import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.bot.CommandDispatcher;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;

import java.util.Set;

@Slf4j
public class DefaultCommandDispatcher implements CommandDispatcher {
    @Override
    public void dispatch(final String roomId, final Set<Plugin> plugins, final RoomMessage message) {
        final Command command = Command.of(message);
        if (command == null) return;

        for (Plugin plugin : plugins) {
            final PluginMessageHandlingResult result = plugin.onCommand(command);
            switch (result) {
                case NOT_INTENDED_FOR_THIS_PLUGIN:
                    continue;
                case SUCCESS:
                    return;
                case BAD_REQUEST:
                    break;
                case FAILURE:
                    break;
            }
        }

        // No plugin found that can handle this message, so let's give some help
        for (Plugin plugin : plugins) {
            final PluginMessageHandlingResult result = plugin.onUnhandledCommand(command);
            if (result == PluginMessageHandlingResult.SUCCESS) return;
        }

        log.error("Could not handle message: {}", message);
    }
}
