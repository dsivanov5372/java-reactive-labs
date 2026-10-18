package org.example.generator;

import org.example.model.WeatherType;

import java.util.Random;
import java.util.function.Function;

public final class TemperatureGenerator implements Function<WeatherType, Double> {
    private final Random random;

    public TemperatureGenerator() {
        this(new Random());
    }

    public TemperatureGenerator(Random random) {
        this.random = random;
    }

    @Override
    public Double apply(WeatherType weatherType) {
        double temperature = switch (weatherType) {
            case SNOW -> random.nextDouble(-20.0, 0.0);
            case RAIN -> random.nextDouble(2.0, 28.0);
            case THUNDERSTORM -> random.nextDouble(12.0, 34.0);
            case CLEAR, CLOUDY -> random.nextDouble(-15.0, 37.0);
        };
        return Math.round(temperature * 10.0) / 10.0;
    }
}
