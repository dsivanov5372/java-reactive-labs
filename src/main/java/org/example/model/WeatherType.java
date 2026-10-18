package org.example.model;

public enum WeatherType {
    CLEAR("Ясно"),
    CLOUDY("Облачно"),
    RAIN("Дождь"),
    SNOW("Снег"),
    THUNDERSTORM("Гроза");

    private final String displayName;

    WeatherType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
