package com.jumbo.store.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jumbo.store.json.deserializer.StoreItemDeserializer;

import java.time.LocalTime;
import java.util.Optional;

/*Immutable class*/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = StoreItemDeserializer.class)
public final class StoreItem {

    private final String uuid;
    private final Address address;
    private final Location location;
    private final LocalTime todayOpen;
    private final LocalTime todayClose;
    private final Integer complexNumber;
    private final Integer sapStoreID;
    private final Boolean showWarningMessage;
    private final Boolean collectionPoint;


    public StoreItem(String uuid, Address address, Location location, LocalTime todayOpen, LocalTime todayClose, Integer complexNumber, Integer sapStoreID, Boolean showWarningMessage, Boolean collectionPoint) {
        this.uuid = uuid;
        this.address = address;
        this.location = location;
        this.todayOpen = todayOpen;
        this.todayClose = todayClose;
        this.complexNumber = complexNumber;
        this.sapStoreID = sapStoreID;
        this.showWarningMessage = showWarningMessage;
        this.collectionPoint = collectionPoint;
    }

    public Optional<String> getUuid() {
        return Optional.ofNullable(uuid);
    }

    public Address getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    public Optional<LocalTime> getTodayOpen() {
        return Optional.ofNullable(todayOpen);
    }

    public Optional<LocalTime> getTodayClose() {
        return Optional.ofNullable(todayClose);
    }

    public Optional<Integer> getComplexNumber() {
        return Optional.ofNullable(complexNumber);
    }

    public Optional<Integer> getSapStoreID() {
        return Optional.ofNullable(sapStoreID);
    }

    public Optional<Boolean> getShowWarningMessage() {
        return Optional.ofNullable(showWarningMessage);
    }

    public Optional<Boolean> getCollectionPoint() {
        return Optional.ofNullable(collectionPoint);
    }
}
