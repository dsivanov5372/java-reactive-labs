package org.example.benchmark;

import org.example.generator.WeatherForecastGenerator;
import org.example.model.Location;
import org.example.model.WeatherForecast;
import org.example.statistics.WeatherStatistics;
import org.example.statistics.WeatherStatisticsCalculator;
import org.openjdk.jmh.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 10)
@Measurement(iterations = 5, time = 10)
@Fork(value = 2, jvmArgsAppend = {"-Xms512m", "-Xmx2048m"})
@Threads(1)
public class StatisticsJmhBenchmark {
    @Param({"5000", "50000", "250000"})
    public int size;

    @Param({"0"})
    public long delayMillis;

    @Param({"0"})
    public int splitThreshold;

    private List<WeatherForecast> forecasts;

    @Setup(Level.Trial)
    public void setup() {
        if (splitThreshold < 0) {
            throw new IllegalArgumentException("Split threshold must not be negative");
        }
        if (delayMillis < 0) {
            throw new IllegalArgumentException("Delay threshold mush not be negative");
        }

        forecasts = Stream.generate(new WeatherForecastGenerator(
                        new Random(42), LocalDate.of(2026, 10, 18)))
                .limit(size)
                .toList();
    }

    @Benchmark
    public ConcurrentMap<Location, WeatherStatistics> sequential() {
        return WeatherStatisticsCalculator.calculateWithSequentialStream(forecasts, delayMillis);
    }

    @Benchmark
    public ConcurrentMap<Location, WeatherStatistics> parallel() {
        return WeatherStatisticsCalculator.calculateWithParallelStream(forecasts, delayMillis);
    }

    @Benchmark
    public ConcurrentMap<Location, WeatherStatistics> customSpliterator() {
        return WeatherStatisticsCalculator.calculateWithSpliterator(forecasts, delayMillis, splitThreshold);
    }
}
