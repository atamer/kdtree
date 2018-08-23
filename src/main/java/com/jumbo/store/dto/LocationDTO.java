package com.jumbo.store.dto;

public class LocationDTO {

    private final Double longitude;
    private final Double latitude;
    private final String locationType;

    LocationDTO(Double longitude, Double latitude, String locationType) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationType = locationType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getLocationType() {
        return locationType;
    }
}
