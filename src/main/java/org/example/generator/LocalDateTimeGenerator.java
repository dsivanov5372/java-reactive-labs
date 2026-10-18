package org.example.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Supplier;

public final class LocalDateTimeGenerator implements Supplier<LocalDateTime> {
    private final Random random;
    private final LocalDate baseDate;

    public LocalDateTimeGenerator() {
        this(new Random(), LocalDate.now());
    }

    public LocalDateTimeGenerator(Random random) {
        this(random, LocalDate.now());
    }

    public LocalDateTimeGenerator(LocalDate baseDate) {
        this(new Random(), baseDate);
    }

    public LocalDateTimeGenerator(Random random, LocalDate baseDate) {
        this.random = random;
        this.baseDate = baseDate;
    }

    @Override
    public LocalDateTime get() {
        return baseDate.plusDays(random.nextInt(7)).atTime(random.nextInt(24), 0);
    }
}
