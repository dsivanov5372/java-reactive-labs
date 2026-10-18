package org.example.generator;

import org.example.model.WeatherWarning;
import org.example.model.Wind;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

public final class WeatherWarningGenerator implements BiFunction<LocalDateTime, Wind, Set<WeatherWarning>> {
    private final Random random;

    public WeatherWarningGenerator() {
        this(new Random());
    }

    public WeatherWarningGenerator(Random random) {
        this.random = random;
    }

    @Override
    public Set<WeatherWarning> apply(LocalDateTime dateTime, Wind wind) {
        Set<WeatherWarning> warnings = EnumSet.noneOf(WeatherWarning.class);
        if (wind.getSpeedMetersPerSecond() >= 15.0) {
            warnings.add(WeatherWarning.STRONG_WIND);
        }

        int month = dateTime.getMonthValue();
        int hour = dateTime.getHour();
        if (month >= 4 && month <= 9 && random.nextDouble() < 0.30) {
            warnings.add(WeatherWarning.HIGH_POLLEN);
        }
        if (month >= 5 && month <= 8 && hour >= 10 && hour <= 16 && random.nextDouble() < 0.25) {
            warnings.add(WeatherWarning.HIGH_UV);
        }
        if (random.nextDouble() < 0.10) {
            warnings.add(WeatherWarning.POOR_AIR_QUALITY);
        }
        return Set.copyOf(warnings);
    }
}
