package org.royjacobs.lazybot.weather;

import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;

public class WeatherService {
    public String getForecast(final String city, final String countryIsoCode) {
        String forecast = "Couldn't retrieve forecast";

        try {
            OwmClient owm = new OwmClient();
            WeatherStatusResponse currentWeather = owm.currentWeatherAtCity(city, countryIsoCode);
            if (currentWeather.hasWeatherStatus()) {
                WeatherData weather = currentWeather.getWeatherStatus().get(0);
                if (weather.getPrecipitation() == Integer.MIN_VALUE) {
                    WeatherData.WeatherCondition weatherCondition = weather.getWeatherConditions().get(0);
                    String description = weatherCondition.getDescription();
                    if (description.contains("rain") || description.contains("shower"))
                        forecast = "No rain measures in " + city + " but reports of " + description;
                    else
                        forecast = "No rain measures in " + city +": " + description;
                } else
                    forecast = "It's raining in " + city +": " + weather.getPrecipitation() + " mm/h";
            }
        } catch (Exception e) {
            forecast = "Couldn't retrieve forecast :( " + e;
        }
        return forecast;
    }
}
