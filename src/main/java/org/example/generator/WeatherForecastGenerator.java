package org.example.generator;

import org.example.model.AtmosphericConditions;
import org.example.model.Location;
import org.example.model.WeatherForecast;
import org.example.model.WeatherType;
import org.example.model.WeatherWarning;
import org.example.model.Wind;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class WeatherForecastGenerator implements Supplier<WeatherForecast> {
    private final Supplier<WeatherType> weatherTypeGenerator;
    private final Supplier<LocalDateTime> dateTimeGenerator;
    private final Supplier<Location> locationGenerator;
    private final Function<WeatherType, AtmosphericConditions> conditionsGenerator;
    private final Function<WeatherType, Double> temperatureGenerator;
    private final BiFunction<LocalDateTime, Wind, Set<WeatherWarning>> warningGenerator;
    private long nextId = 1;

    public WeatherForecastGenerator() {
        this(new Random(), LocalDate.now());
    }

    public WeatherForecastGenerator(Random random) {
        this(random, LocalDate.now());
    }

    public WeatherForecastGenerator(LocalDate baseDate) {
        this(new Random(), baseDate);
    }

    public WeatherForecastGenerator(Random random, LocalDate baseDate) {
        this.weatherTypeGenerator = new WeatherTypeGenerator(random);
        this.dateTimeGenerator = new LocalDateTimeGenerator(random, baseDate);
        this.locationGenerator = new LocationGenerator(random);
        this.conditionsGenerator = new AtmosphericConditionsGenerator(random);
        this.temperatureGenerator = new TemperatureGenerator(random);
        this.warningGenerator = new WeatherWarningGenerator(random);
    }

    @Override
    public WeatherForecast get() {
        WeatherType weatherType = weatherTypeGenerator.get();
        LocalDateTime dateTime = dateTimeGenerator.get();
        Location location = locationGenerator.get();
        AtmosphericConditions conditions = conditionsGenerator.apply(weatherType);
        double temperature = temperatureGenerator.apply(weatherType);
        Set<WeatherWarning> warnings = warningGenerator.apply(dateTime, conditions.getWind());
        String summary = weatherType.getDisplayName() + ", " + location.city() + ", " + dateTime;

        return new WeatherForecast(nextId++, summary, dateTime, weatherType,
                temperature, location, conditions, warnings);
    }
}
