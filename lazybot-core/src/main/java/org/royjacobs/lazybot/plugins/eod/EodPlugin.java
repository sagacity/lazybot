package org.royjacobs.lazybot.plugins.eod;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.api.plugins.PluginDescriptor;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EodPlugin implements Plugin {
    private PluginContext context;

    @Override
    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key("eod")
                .build();
    }

    @Override
    public void onStart(final PluginContext context) {
        this.context = context;
    }

    @Override
    public void onStop(final boolean unregistered) {
    }

    @Override
    public PluginMessageHandlingResult onCommand(final Command command) {
        if (command.getCommand().equalsIgnoreCase("eod")) {
            context.getRoomApi().sendNotification(new Notification("eod", "text", "Engineer on Duty, am I right?"));
            return PluginMessageHandlingResult.SUCCESS;
        }
        return PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
    }
}
