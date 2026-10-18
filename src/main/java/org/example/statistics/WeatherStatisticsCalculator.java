package org.example.statistics;

import org.example.model.Location;
import org.example.model.WeatherForecast;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class WeatherStatisticsCalculator {
    private WeatherStatisticsCalculator() {
    }

    public static Map<Location, WeatherStatistics> calculateWithLoop(List<WeatherForecast> forecasts) {
        Map<Location, WeatherStatisticsAccumulator> accumulators = new HashMap<>();
        for (WeatherForecast forecast : forecasts) {
            WeatherStatisticsAccumulator accumulator = accumulators.computeIfAbsent(
                    forecast.getLocation(), location -> new WeatherStatisticsAccumulator());
            accumulator.add(forecast);
        }

        Map<Location, WeatherStatistics> result = new HashMap<>();
        for (Map.Entry<Location, WeatherStatisticsAccumulator> entry : accumulators.entrySet()) {
            result.put(entry.getKey(), entry.getValue().finish());
        }
        return result;
    }

    public static Map<Location, WeatherStatistics> calculateWithStandardCollectors(
            List<WeatherForecast> forecasts) {
        Collector<WeatherForecast, ?, DoubleSummaryStatistics> temperatures = Collectors.summarizingDouble(
                WeatherForecast::getTemperature);

        Collector<WeatherForecast, ?, IntSummaryStatistics> humidity = Collectors.summarizingInt(
                forecast -> forecast.getAtmosphericConditions().getHumidityPercent());

        Collector<WeatherForecast, ?, PressureAndWind> pressureAndWind = Collectors.teeing(
                Collectors.averagingInt(
                        forecast -> forecast.getAtmosphericConditions().getPressureMmHg()),
                Collectors.averagingDouble(
                        forecast -> forecast.getAtmosphericConditions().getWind().getSpeedMetersPerSecond()),
                PressureAndWind::new);

        Collector<WeatherForecast, ?, AtmosphericSummary> atmosphere = Collectors.teeing(
                humidity, pressureAndWind, AtmosphericSummary::new);

        return forecasts.stream().collect(Collectors.groupingBy(
                WeatherForecast::getLocation,
                Collectors.teeing(temperatures, atmosphere, (temperature, conditions) ->
                        new WeatherStatistics(
                                conditions.humidity().getCount(),
                                temperature.getAverage(),
                                temperature.getMin(),
                                temperature.getMax(),
                                conditions.humidity().getAverage(),
                                conditions.pressureAndWind().averagePressureMmHg(),
                                conditions.pressureAndWind().averageWindSpeed()))));
    }

    public static Map<Location, WeatherStatistics> calculateWithCustomCollector(
            List<WeatherForecast> forecasts) {
        return forecasts.stream().collect(new WeatherStatisticsCollector());
    }

    private record PressureAndWind(double averagePressureMmHg, double averageWindSpeed) {
    }

    private record AtmosphericSummary(IntSummaryStatistics humidity, PressureAndWind pressureAndWind) {
    }
}
