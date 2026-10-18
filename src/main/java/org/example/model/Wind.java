package org.example.model;

import java.util.Locale;
import java.util.Objects;

public final class Wind {
    private final double speedMetersPerSecond;
    private final int directionDegrees;

    public Wind(double speedMetersPerSecond, int directionDegrees) {
        this.speedMetersPerSecond = speedMetersPerSecond;
        this.directionDegrees = directionDegrees;
    }

    public double getSpeedMetersPerSecond() {
        return speedMetersPerSecond;
    }

    public int getDirectionDegrees() {
        return directionDegrees;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Wind wind)) {
            return false;
        }
        return Double.compare(speedMetersPerSecond, wind.speedMetersPerSecond) == 0
                && directionDegrees == wind.directionDegrees;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speedMetersPerSecond, directionDegrees);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "ветер %.1f м/с, направление ветра - %d градусов",
                speedMetersPerSecond, directionDegrees);
    }
}
