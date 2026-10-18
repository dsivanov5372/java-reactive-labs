package org.example.generator;

import org.example.model.WeatherType;

import java.util.Random;
import java.util.function.Supplier;

public final class WeatherTypeGenerator implements Supplier<WeatherType> {
    private static final WeatherType[] WEATHER_TYPES = WeatherType.values();

    private final Random random;

    public WeatherTypeGenerator() {
        this(new Random());
    }

    public WeatherTypeGenerator(Random random) {
        this.random = random;
    }

    @Override
    public WeatherType get() {
        return WEATHER_TYPES[random.nextInt(WEATHER_TYPES.length)];
    }
}
