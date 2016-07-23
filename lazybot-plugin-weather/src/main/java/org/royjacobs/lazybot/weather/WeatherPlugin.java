package org.royjacobs.lazybot.weather;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.api.plugins.PluginDescriptor;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;

public class WeatherPlugin implements Plugin {
    private PluginContext context;

    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key("weather")
                .build();
    }

    public void onStart(PluginContext context) {
        this.context = context;
    }

    public void onStop(boolean removed) {

    }

    public PluginMessageHandlingResult onCommand(Command command) {
        if (command.getCommand().equalsIgnoreCase("weather")) {
            context.getRoomApi().sendNotification(new Notification("weather", "text", "Rain, probably?"));
            return PluginMessageHandlingResult.SUCCESS;
        }
        return PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
    }
}
