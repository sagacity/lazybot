package org.royjacobs.lazybot.bot;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
public class DefaultCommandDispatcher implements CommandDispatcher {
    @Override
    public void dispatch(final String roomId, final Set<Plugin> plugins, final RoomMessage message) {
        final List<String> cmdLine = Splitter.on(CharMatcher.WHITESPACE)
                .trimResults()
                .omitEmptyStrings()
                .splitToList(message.getItem().getMessage().getMessage());
        if (cmdLine.isEmpty()) return;
        if (!cmdLine.get(0).equalsIgnoreCase("/lazybot")) return;

        final Command command = Command.builder()
                .originalMessage(message)
                .command(cmdLine.size() > 1 ? cmdLine.get(1) : "help")
                .args(cmdLine.size() > 2 ? cmdLine.subList(2, cmdLine.size()) : Collections.emptyList())
                .build();

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
