package org.example.statistics;

import org.example.model.Location;
import org.example.model.WeatherForecast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class WeatherStatisticsCollector implements Collector<
        WeatherForecast,
        Map<Location, WeatherStatisticsAccumulator>,
        ConcurrentMap<Location, WeatherStatistics>> {

    private final long delayMillis;

    public WeatherStatisticsCollector() {
        this(0);
    }

    public WeatherStatisticsCollector(long delayMillis) {
        if (delayMillis < 0) {
            throw new IllegalArgumentException("delayMillis must not be negative");
        }
        this.delayMillis = delayMillis;
    }

    @Override
    public Supplier<Map<Location, WeatherStatisticsAccumulator>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Location, WeatherStatisticsAccumulator>, WeatherForecast> accumulator() {
        return (statistics, forecast) -> statistics
                .computeIfAbsent(forecast.getLocation(), location -> new WeatherStatisticsAccumulator())
                .add(forecast, delayMillis);
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
    public Function<Map<Location, WeatherStatisticsAccumulator>, ConcurrentMap<Location, WeatherStatistics>> finisher() {
        return accumulators -> {
            ConcurrentMap<Location, WeatherStatistics> statistics = new ConcurrentHashMap<>();
            accumulators.forEach((location, accumulator) -> statistics.put(location, accumulator.finish()));
            return statistics;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
