package com.jumbo.store.kdtree;

import com.google.common.geometry.S2LatLng;
import com.jumbo.store.exception.GeoLocationException;
import com.jumbo.store.json.Location;
import com.jumbo.store.json.StoreItem;

public class DistanceStoreItem {
    private final Double distance;
    private final StoreItem storeItem;

    public DistanceStoreItem(StoreItem storeItem, Point point) throws GeoLocationException {
        Location location = storeItem.getLocation();
        if (location.getLatitude().isPresent() && location.getLongitude().isPresent()) {
            distance = distanceBetween(location.getLatitude().get(), location.getLongitude().get(), point.getLatitude(), point.getLongtitude());
        } else {
            throw new GeoLocationException(null);
        }
        this.storeItem = storeItem;

    }

    private Double distanceBetween(Double lat1, Double lon1, Double lat2, Double lon2) {
        return S2LatLng.fromDegrees(lat1, lon1).getDistance(S2LatLng.fromDegrees(lat2, lon2)).radians();
    }

    public Double getDistance() {
        return distance;
    }

    public StoreItem getStoreItem() {
        return storeItem;
    }
}
