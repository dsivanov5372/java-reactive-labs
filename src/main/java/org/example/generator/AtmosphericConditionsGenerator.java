package org.example.generator;

import org.example.model.AtmosphericConditions;
import org.example.model.WeatherType;

import java.util.Random;
import java.util.function.Function;

public final class AtmosphericConditionsGenerator implements Function<WeatherType, AtmosphericConditions> {
    private final Random random;
    private final WindGenerator windGenerator;

    public AtmosphericConditionsGenerator() {
        this(new Random());
    }

    public AtmosphericConditionsGenerator(Random random) {
        this.random = random;
        this.windGenerator = new WindGenerator(random);
    }

    @Override
    public AtmosphericConditions apply(WeatherType weatherType) {
        return switch (weatherType) {
            case CLEAR -> generateInRanges(20, 60, 758, 776, 0.0, 8.0);
            case CLOUDY -> generateInRanges(40, 85, 746, 769, 0.0, 12.0);
            case RAIN -> generateInRanges(60, 100, 735, 761, 1.0, 15.0);
            case SNOW -> generateInRanges(65, 100, 739, 765, 0.0, 15.0);
            case THUNDERSTORM -> generateInRanges(75, 100, 728, 754, 8.0, 25.0);
        };
    }

    private AtmosphericConditions generateInRanges(
            int minHumidity, int maxHumidity,
            int minPressureMmHg, int maxPressureMmHg,
            double minWindSpeed, double maxWindSpeed) {
        return new AtmosphericConditions(
                random.nextInt(minHumidity, maxHumidity + 1),
                random.nextInt(minPressureMmHg, maxPressureMmHg + 1),
                windGenerator.generateInRange(minWindSpeed, maxWindSpeed));
    }
}
