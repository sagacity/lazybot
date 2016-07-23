package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.bot.plugins.Plugin;
import org.royjacobs.lazybot.bot.plugins.PluginMessageHandlingResult;

import java.util.Set;

import static org.royjacobs.lazybot.bot.plugins.PluginMessageHandlingResult.*;

public class DefaultCommandDispatcher implements CommandDispatcher {
    @Override
    public PluginMessageHandlingResult dispatch(final Set<Plugin> plugins, Command command) {
        for (Plugin plugin : plugins) {
            final PluginMessageHandlingResult result = plugin.onCommand(command);
            switch (result) {
                case NOT_INTENDED_FOR_THIS_PLUGIN:
                    continue;
                case SUCCESS:
                    return SUCCESS;
                case BAD_REQUEST:
                    return BAD_REQUEST;
                case FAILURE:
                    return FAILURE;
            }
        }

        // No plugin found that can handle this message, so let's give some help
        for (Plugin plugin : plugins) {
            final PluginMessageHandlingResult result = plugin.onUnhandledCommand(command);
            if (result == PluginMessageHandlingResult.SUCCESS) return SUCCESS;
        }

        return FAILURE;
    }
}
