package org.royjacobs.lazybot.weather;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.api.plugins.PluginDescriptor;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;

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
        configData = (WeatherConfigData) context.getConfigData();
    }

    public void onStop(boolean unregistered) {
    }

    public PluginMessageHandlingResult onCommand(Command command) {
        if (command.getCommand().equalsIgnoreCase("weather")) {
            final String forecast = weatherService.getForecast(configData.city, configData.country);
            context.getRoomApi().sendMessage(forecast, null);
            return PluginMessageHandlingResult.SUCCESS;
        }
        return PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
    }
}
