package org.example.generator;

import org.example.model.Wind;

import java.util.Random;
import java.util.function.Supplier;

public final class WindGenerator implements Supplier<Wind> {
    private final Random random;

    public WindGenerator() {
        this(new Random());
    }

    public WindGenerator(Random random) {
        this.random = random;
    }

    @Override
    public Wind get() {
        return generateInRange(0.0, 30.0);
    }

    Wind generateInRange(double minSpeed, double maxSpeed) {
        return new Wind(random.nextDouble(minSpeed, maxSpeed), random.nextInt(360));
    }
}
