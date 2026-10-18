package org.example.model;

public record Location(long id, String country, String region, String city) {
    @Override
    public String toString() {
        return country + ", " + region + ", " + city;
    }
}
