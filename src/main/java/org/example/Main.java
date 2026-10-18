package org.example;

import org.example.generator.WeatherForecastGenerator;
import org.example.statistics.StatisticsBenchmark;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Main {
    static void main() {
        List<Integer> counts = List.of(5000, 50000, 250000);
        for (Integer count : counts) {
            var forecasts = Stream.generate(new WeatherForecastGenerator(new Random(42)))
                    .limit(count)
                    .toList();
            StatisticsBenchmark.run(forecasts);
        }
    }
}
