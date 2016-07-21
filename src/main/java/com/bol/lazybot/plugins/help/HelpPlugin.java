package com.bol.lazybot.plugins.help;

import com.bol.lazybot.bot.Command;
import com.bol.lazybot.hipchat.client.Notification;
import com.bol.lazybot.bot.plugins.Plugin;
import com.bol.lazybot.bot.plugins.PluginContext;
import com.bol.lazybot.bot.plugins.PluginDescriptor;
import com.bol.lazybot.bot.plugins.PluginMessageHandlingResult;

import java.util.stream.Collectors;

public class HelpPlugin implements Plugin {
    private PluginContext context;

    @Override
    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key("help")
                .build();
    }

    @Override
    public void onStart(PluginContext context) {
        this.context = context;
    }

    @Override
    public void onStop(boolean removed) {

    }

    @Override
    public PluginMessageHandlingResult onCommand(final Command command) {
        return PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
    }

    @Override
    public PluginMessageHandlingResult onUnhandledCommand(final Command command) {
        final String commands = context.getAllDescriptors().stream().map(PluginDescriptor::getKey).collect(Collectors.joining(", "));

        context.getRoomApi().sendNotification(new Notification("help", "text", "I can help you with the following commands: " + commands));
        return PluginMessageHandlingResult.SUCCESS;
    }
}
