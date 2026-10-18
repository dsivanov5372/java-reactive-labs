package org.example.model;

import java.util.Locale;
import java.util.Objects;

public final class AtmosphericConditions {
    private final int humidityPercent;
    private final int pressureMmHg;
    private final Wind wind;

    public AtmosphericConditions(int humidityPercent, int pressureMmHg, Wind wind) {
        this.humidityPercent = humidityPercent;
        this.pressureMmHg = pressureMmHg;
        this.wind = wind;
    }

    public int getHumidityPercent() {
        return humidityPercent;
    }

    public int getPressureMmHg() {
        return pressureMmHg;
    }

    public Wind getWind() {
        return wind;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AtmosphericConditions conditions)) {
            return false;
        }
        return humidityPercent == conditions.humidityPercent
                && pressureMmHg == conditions.pressureMmHg
                && Objects.equals(wind, conditions.wind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(humidityPercent, pressureMmHg, wind);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "влажность %d%%, давление %d мм рт. ст., %s",
                humidityPercent, pressureMmHg, wind);
    }
}
