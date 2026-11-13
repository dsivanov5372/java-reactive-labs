package org.example.statistics;

import org.example.model.Location;
import org.example.model.WeatherForecast;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        return calculateWithSequentialStream(forecasts, 0);
    }

    public static ConcurrentMap<Location, WeatherStatistics> calculateWithSequentialStream(
            List<WeatherForecast> forecasts, long delayMillis) {
        return forecasts.stream().collect(new WeatherStatisticsCollector(delayMillis));
    }

    public static ConcurrentMap<Location, WeatherStatistics> calculateWithParallelStream(
            List<WeatherForecast> forecasts, long delayMillis) {
        return forecasts.parallelStream().collect(new WeatherStatisticsCollector(delayMillis));
    }

    public static ConcurrentMap<Location, WeatherStatistics> calculateWithSpliterator(
            List<WeatherForecast> forecasts, long delayMillis, int splitThreshold) {
        return StreamSupport.stream(new WeatherForecastSpliterator(forecasts, splitThreshold), true)
                .collect(new WeatherStatisticsCollector(delayMillis));
    }

    private record PressureAndWind(double averagePressureMmHg, double averageWindSpeed) {
    }

    private record AtmosphericSummary(IntSummaryStatistics humidity, PressureAndWind pressureAndWind) {
    }
}
