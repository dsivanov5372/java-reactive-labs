package org.example.generator;

import org.example.model.Location;

import java.util.Random;
import java.util.function.Supplier;

public final class LocationGenerator implements Supplier<Location> {
    private static final Location[] CITIES = {
            new Location(1, "Россия", "Москва", "Москва"),
            new Location(2, "Россия", "Санкт-Петербург", "Санкт-Петербург"),
            new Location(3, "Россия", "Республика Татарстан", "Казань"),
            new Location(4, "Россия", "Новосибирская область", "Новосибирск"),
            new Location(5, "Россия", "Краснодарский край", "Сочи")
    };

    private final Random random;

    public LocationGenerator() {
        this(new Random());
    }

    public LocationGenerator(Random random) {
        this.random = random;
    }

    @Override
    public Location get() {
        return CITIES[random.nextInt(CITIES.length)];
    }
}
