package org.example.statistics;

import org.example.model.Location;
import org.example.model.WeatherForecast;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class StatisticsBenchmark {
    private static final int MEASURED_ROUNDS = 5;

    private StatisticsBenchmark() {
    }

    public static void run(List<WeatherForecast> forecasts) {
        System.out.printf("%nКоличество прогнозов: %d%n", forecasts.size());
        measure("Цикл", WeatherStatisticsCalculator::calculateWithLoop, forecasts);
        measure("Стандартный коллектор", WeatherStatisticsCalculator::calculateWithStandardCollectors, forecasts);
        measure("Собственный коллектор", WeatherStatisticsCalculator::calculateWithCustomCollector, forecasts);
    }

    private static void measure(String name,
                                Function<List<WeatherForecast>, Map<Location, WeatherStatistics>> calculator,
                                List<WeatherForecast> forecasts) {
        System.out.printf("%n%s%n", name);
        System.out.printf("%6s %12s%n", "Повтор", "Время, мс");

        Map<Location, WeatherStatistics> statistics = Map.of();
        for (int round = 0; round < MEASURED_ROUNDS; round++) {
            long start = System.nanoTime();
            statistics = calculator.apply(forecasts);
            long end = System.nanoTime();

            System.out.printf(Locale.ROOT, "%6d %12.3f%n",
                    round, (end - start) / 1_000_000.0);
        }
        printStatistics(statistics);
    }

    private static void printStatistics(Map<Location, WeatherStatistics> statistics) {
        System.out.println("Статистика по локациям:");
        System.out.printf("%-19s %10s %9s %9s %9s %10s %16s %12s%n",
                "Город", "Прогнозов", "Tmin, °C", "Tср, °C", "Tmax, °C", "Влажн., %", "Давл., мм рт. ст.", "Ветер, м/с");
        statistics.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> entry.getKey().id()))
                .forEach(entry -> {
                    var value = entry.getValue();
                    System.out.printf(Locale.ROOT, "%-19s %10d %9.2f %9.2f %9.2f %10.2f %16.2f %12.2f%n",
                            entry.getKey().city(), value.forecastCount(), value.minTemperature(),
                            value.averageTemperature(), value.maxTemperature(), value.averageHumidity(),
                            value.averagePressureMmHg(), value.averageWindSpeed());
                });
    }
}
