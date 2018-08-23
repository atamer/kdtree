package com.jumbo.store.kdtree;

import com.google.common.geometry.S1Angle;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.jumbo.store.constants.Constants;
import com.jumbo.store.exception.GeoLocationException;

public class Box {


    private final S2LatLngRect rect;
    private Double middleLongitude = null;
    private Double middleLatitude = null;


    public Box(Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude) throws GeoLocationException {

        checkGeoLocationLatitude(minLatitude);
        checkGeoLocationLatitude(maxLatitude);
        checkGeoLocationLongitude(minLongitude);
        checkGeoLocationLongitude(maxLongitude);

        rect = new S2LatLngRect(S2LatLng.fromDegrees(minLatitude, minLongitude), S2LatLng.fromDegrees(maxLatitude, maxLongitude));

        middleLatitude = (maxLatitude + minLatitude) / 2;
        middleLongitude = (minLongitude + maxLongitude) / 2;

    }


    private void checkGeoLocationLatitude(Double geolocation) throws GeoLocationException {
        if (geolocation == null || Constants.MIN_LATITUDE.compareTo(geolocation) > 0 || Constants.MAX_LATITUDE.compareTo(geolocation) < 0) {
            throw new GeoLocationException(geolocation);
        }
    }


    private void checkGeoLocationLongitude(Double geolocation) throws GeoLocationException {
        if (geolocation == null || Constants.MIN_LONGITUDE.compareTo(geolocation) > 0 || Constants.MAX_LONGITUDE.compareTo(geolocation) < 0) {
            throw new GeoLocationException(geolocation);
        }
    }

    public Double getMiddleLatitude() {
        return middleLatitude;
    }

    public Double getMiddleLongitude() {
        return middleLongitude;
    }

    public Double distanceTo(Point point) {
        S1Angle angle = rect.getDistance(S2LatLng.fromDegrees(point.getLatitude(), point.getLongtitude()));
        return angle.radians();
    }
}
