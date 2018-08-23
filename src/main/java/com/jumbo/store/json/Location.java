package com.jumbo.store.json;


import java.util.Optional;

/*Immutable class*/
public final class Location {
    private final Double longitude;
    private final Double latitude;
    private final String locationType;

    public Location(Double longitude, Double latitude, String locationType) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationType = locationType;
    }

    public Optional<Double> getLongitude() {
        return Optional.ofNullable(longitude);
    }

    public Optional<Double> getLatitude() {
        return Optional.ofNullable(latitude);
    }

    public Optional<String> getLocationType() {
        return Optional.ofNullable(locationType);
    }
}
