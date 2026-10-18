package org.example.statistics;

import org.example.model.Location;
import org.example.model.WeatherForecast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class WeatherStatisticsCollector implements Collector<
        WeatherForecast,
        Map<Location, WeatherStatisticsAccumulator>,
        Map<Location, WeatherStatistics>> {

    @Override
    public Supplier<Map<Location, WeatherStatisticsAccumulator>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Location, WeatherStatisticsAccumulator>, WeatherForecast> accumulator() {
        return (statistics, forecast) -> statistics
                .computeIfAbsent(forecast.getLocation(), location -> new WeatherStatisticsAccumulator())
                .add(forecast);
    }

    @Override
    public BinaryOperator<Map<Location, WeatherStatisticsAccumulator>> combiner() {
        return (left, right) -> {
            right.forEach((location, accumulator) -> left.merge(location, accumulator, (first, second) -> {
                first.combine(second);
                return first;
            }));
            return left;
        };
    }

    @Override
    public Function<Map<Location, WeatherStatisticsAccumulator>, Map<Location, WeatherStatistics>> finisher() {
        return accumulators -> {
            Map<Location, WeatherStatistics> statistics = new HashMap<>();
            accumulators.forEach((location, accumulator) -> statistics.put(location, accumulator.finish()));
            return statistics;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
