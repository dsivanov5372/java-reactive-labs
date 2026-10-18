package org.example.statistics;

import org.example.model.AtmosphericConditions;
import org.example.model.WeatherForecast;

final class WeatherStatisticsAccumulator {
    private long forecastCount;
    private double temperatureSum;
    private double minTemperature = Double.POSITIVE_INFINITY;
    private double maxTemperature = Double.NEGATIVE_INFINITY;
    private long humiditySum;
    private long pressureSum;
    private double windSpeedSum;

    void add(WeatherForecast forecast) {
        forecastCount++;
        double temperature = forecast.getTemperature();
        temperatureSum += temperature;
        minTemperature = Math.min(minTemperature, temperature);
        maxTemperature = Math.max(maxTemperature, temperature);

        AtmosphericConditions conditions = forecast.getAtmosphericConditions();
        humiditySum += conditions.getHumidityPercent();
        pressureSum += conditions.getPressureMmHg();
        windSpeedSum += conditions.getWind().getSpeedMetersPerSecond();
    }

    void combine(WeatherStatisticsAccumulator other) {
        forecastCount += other.forecastCount;
        temperatureSum += other.temperatureSum;
        minTemperature = Math.min(minTemperature, other.minTemperature);
        maxTemperature = Math.max(maxTemperature, other.maxTemperature);
        humiditySum += other.humiditySum;
        pressureSum += other.pressureSum;
        windSpeedSum += other.windSpeedSum;
    }

    WeatherStatistics finish() {
        return new WeatherStatistics(
                forecastCount,
                temperatureSum / forecastCount,
                minTemperature,
                maxTemperature,
                (double) humiditySum / forecastCount,
                (double) pressureSum / forecastCount,
                windSpeedSum / forecastCount
        );
    }
}
