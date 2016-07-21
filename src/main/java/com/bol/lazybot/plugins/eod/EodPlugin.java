package com.bol.lazybot.plugins.eod;

import com.bol.lazybot.bot.Command;
import com.bol.lazybot.hipchat.client.Notification;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.PluginContext;
import com.bol.lazybot.plugins.PluginDescriptor;
import com.bol.lazybot.plugins.PluginMessageHandlingResult;
import com.bol.lazybot.hipchat.server.webhooks.dto.RoomMessage;
import lombok.extern.slf4j.Slf4j;

import static com.bol.lazybot.plugins.PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
import static com.bol.lazybot.plugins.PluginMessageHandlingResult.SUCCESS;

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
    public void onStop(final boolean removed) {
    }

    @Override
    public PluginMessageHandlingResult onCommand(final Command command) {
        if (command.getCommand().equalsIgnoreCase("eod")) {
            context.getRoomApi().sendNotification(new Notification("eod", "text", "Engineer on Duty, am I right?"));
            return SUCCESS;
        }
        return NOT_INTENDED_FOR_THIS_PLUGIN;
    }
}
