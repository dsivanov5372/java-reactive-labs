package org.example.statistics;

import org.example.model.WeatherForecast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class WeatherForecastSpliterator implements Spliterator<WeatherForecast> {
    private final List<WeatherForecast> forecasts;
    private final int fence;
    private final int splitThreshold;
    private int index;

    public WeatherForecastSpliterator(List<WeatherForecast> forecasts, int splitThreshold) {
        Objects.requireNonNull(forecasts, "forecasts");
        if (splitThreshold < 1) {
            throw new IllegalArgumentException("Split threshold must be positive");
        }
        this.forecasts = forecasts instanceof RandomAccess ? forecasts : new ArrayList<>(forecasts);
        this.fence = this.forecasts.size();
        this.splitThreshold = splitThreshold;
    }

    private WeatherForecastSpliterator(List<WeatherForecast> forecasts, int index, int fence, int splitThreshold) {
        this.forecasts = forecasts;
        this.index = index;
        this.fence = fence;
        this.splitThreshold = splitThreshold;
    }

    @Override
    public Spliterator<WeatherForecast> trySplit() {
        int remaining = fence - index;
        if (remaining <= splitThreshold || remaining <= 1) {
            return null;
        }
        int middle = index + remaining / 2;
        var prefix = new WeatherForecastSpliterator(forecasts, index, middle, splitThreshold);
        index = middle;
        return prefix;
    }

    @Override
    public boolean tryAdvance(Consumer<? super WeatherForecast> action) {
        if (index >= fence) {
            return false;
        }
        action.accept(forecasts.get(index++));
        return true;
    }

    @Override
    public long estimateSize() {
        return fence - index;
    }

    @Override
    public int characteristics() {
        return SIZED | SUBSIZED;
    }
}
