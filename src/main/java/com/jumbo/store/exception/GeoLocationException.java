package com.jumbo.store.exception;

public class GeoLocationException extends Exception {

    public GeoLocationException(Double geoLocation) {
        super("Invalid Geolocation  " + geoLocation);
    }


}
