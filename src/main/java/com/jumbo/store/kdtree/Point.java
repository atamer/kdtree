package com.jumbo.store.kdtree;

public class Point {


    private final Double latitude;
    private final Double longtitude;

    public Point(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }
}
