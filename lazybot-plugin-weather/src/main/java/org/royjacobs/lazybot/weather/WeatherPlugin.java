package org.royjacobs.lazybot.weather;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.api.plugins.*;

public class WeatherPlugin implements Plugin {
    private final WeatherService weatherService;
    private PluginContext context;
    private WeatherConfigData configData;

    public WeatherPlugin() {
        weatherService = new WeatherService();
    }

    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key("weather")
                .configDataClass(WeatherConfigData.class)
                .build();
    }

    public void onStart(PluginContext context) {
        this.context = context;
        configData = (WeatherConfigData)context.getConfigData();
    }

    public void onStop(boolean removed) {

    }

    public PluginMessageHandlingResult onCommand(Command command) {
        if (command.getCommand().equalsIgnoreCase("weather")) {
            final String forecast = weatherService.getForecast(configData.city, configData.country);
            context.getRoomApi().sendNotification(new Notification("weather", "text", forecast));
            return PluginMessageHandlingResult.SUCCESS;
        }
        return PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
    }
}
