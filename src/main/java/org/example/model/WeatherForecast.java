package org.example.model;

import java.time.LocalDateTime;
import java.util.Set;

public final class WeatherForecast {
    private final long id;
    private final String summary;
    private final LocalDateTime dateTime;
    private final WeatherType weatherType;
    private final double temperature;
    private final Location location;
    private final AtmosphericConditions atmosphericConditions;
    private final Set<WeatherWarning> warnings;

    public WeatherForecast(long id, String summary, LocalDateTime dateTime, WeatherType weatherType,
                           double temperature, Location location, AtmosphericConditions atmosphericConditions,
                           Set<WeatherWarning> warnings) {
        this.id = id;
        this.summary = summary;
        this.dateTime = dateTime;
        this.weatherType = weatherType;
        this.temperature = temperature;
        this.location = location;
        this.atmosphericConditions = atmosphericConditions;
        this.warnings = Set.copyOf(warnings);
    }

    public long getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTemperature(long delayMillis) {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Temperature read was interrupted", e);
        }
        return temperature;
    }

    public Location getLocation() {
        return location;
    }

    public AtmosphericConditions getAtmosphericConditions() {
        return atmosphericConditions;
    }

    public Set<WeatherWarning> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "Прогноз под id " + id + " на " + dateTime + ": " + location
                + ", " + weatherType + ", " + temperature
                + " °C, " + atmosphericConditions + ", предупреждения: " + warnings + ".";
    }
}
