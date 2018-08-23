package com.jumbo.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jumbo.store.json.StoreItem;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StoreItemDTO extends ResourceSupport {

    private final String uuid;
    private final AddressDTO address;
    private final LocationDTO location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm")
    private final LocalTime todayOpen;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm")
    private final LocalTime todayClose;
    private final Integer complexNumber;
    private final Integer sapStoreID;
    private final Boolean showWarningMessage;
    private final Boolean collectionPoint;

    private StoreItemDTO(String uuid, AddressDTO address, LocationDTO location, LocalTime todayOpen, LocalTime todayClose, Integer complexNumber, Integer sapStoreID, Boolean showWarningMessage, Boolean collectionPoint) {
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


    private StoreItemDTO(String uuid, AddressDTO address, LocationDTO location) {
        this.uuid = uuid;
        this.address = address;
        this.location = location;
        todayOpen = null;
        todayClose = null;
        complexNumber = null;
        sapStoreID = null;
        showWarningMessage = null;
        collectionPoint = null;

    }

    public static List<StoreItemDTO> convertToDTOMinimal(Stream<StoreItem> storeItemStream) {
        return storeItemStream.map(storeItem -> new StoreItemDTO(storeItem.getUuid().orElse(null), new AddressDTO(storeItem.getAddress().getAddressName().orElse(null)), new LocationDTO(storeItem.getLocation().getLongitude().orElse(null), storeItem.getLocation().getLatitude().orElse(null), storeItem.getLocation().getLocationType().orElse(null)))).collect(Collectors.toList());
    }

    public static List<StoreItemDTO> convertToDTO(Stream<StoreItem> storeItemStream) {
        return storeItemStream.map(storeItem -> new StoreItemDTO(storeItem.getUuid().orElse(null), new AddressDTO(storeItem.getAddress().getCity().orElse(null), storeItem.getAddress().getPostalCode().orElse(null), storeItem.getAddress().getStreet().orElse(null), storeItem.getAddress().getStreet2().orElse(null), storeItem.getAddress().getStreet3().orElse(null), storeItem.getAddress().getAddressName().orElse(null)), new LocationDTO(storeItem.getLocation().getLongitude().orElse(null), storeItem.getLocation().getLatitude().orElse(null), storeItem.getLocation().getLocationType().orElse(null)), storeItem.getTodayOpen().orElse(null), storeItem.getTodayClose().orElse(null), storeItem.getComplexNumber().orElse(null), storeItem.getSapStoreID().orElse(null), storeItem.getShowWarningMessage().orElse(null), storeItem.getCollectionPoint().orElse(null))).collect(Collectors.toList());
    }

    public static StoreItemDTO convertToDTO(StoreItem storeItem) {
        return new StoreItemDTO(storeItem.getUuid().orElse(null), new AddressDTO(storeItem.getAddress().getCity().orElse(null), storeItem.getAddress().getPostalCode().orElse(null), storeItem.getAddress().getStreet().orElse(null), storeItem.getAddress().getStreet2().orElse(null), storeItem.getAddress().getStreet3().orElse(null), storeItem.getAddress().getAddressName().orElse(null)), new LocationDTO(storeItem.getLocation().getLongitude().orElse(null), storeItem.getLocation().getLatitude().orElse(null), storeItem.getLocation().getLocationType().orElse(null)), storeItem.getTodayOpen().orElse(null), storeItem.getTodayClose().orElse(null), storeItem.getComplexNumber().orElse(null), storeItem.getSapStoreID().orElse(null), storeItem.getShowWarningMessage().orElse(null), storeItem.getCollectionPoint().orElse(null));
    }

    public String getUuid() {
        return uuid;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public LocalTime getTodayOpen() {
        return todayOpen;
    }

    public LocalTime getTodayClose() {
        return todayClose;
    }

    public Integer getComplexNumber() {
        return complexNumber;
    }

    public Integer getSapStoreID() {
        return sapStoreID;
    }

    public Boolean getShowWarningMessage() {
        return showWarningMessage;
    }

    public Boolean getCollectionPoint() {
        return collectionPoint;
    }
}
