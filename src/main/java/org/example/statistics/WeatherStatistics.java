package org.example.statistics;

public record WeatherStatistics(long forecastCount,
                                double averageTemperature,
                                double minTemperature,
                                double maxTemperature,
                                double averageHumidity,
                                double averagePressureMmHg,
                                double averageWindSpeed) {
}
