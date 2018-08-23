package com.jumbo.store.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.jumbo.store.json.Address;
import com.jumbo.store.json.Location;
import com.jumbo.store.json.StoreItem;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Macintosh on 27/10/2017.
 */


public class StoreItemDeserializer extends JsonDeserializer<StoreItem> {

    private static final String pattern = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
    private final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm");

    private String getOptionalString(JsonNode node) {
        if (node == null || node.isNull() || !node.isTextual()) {
            return null;
        } else {
            return node.asText();
        }
    }

    private Double getOptionalDouble(JsonNode node) {
        String nodeStr = getOptionalString(node);
        if (nodeStr != null && NumberUtils.isCreatable(nodeStr)) {
            return NumberUtils.createDouble(nodeStr);
        } else {
            return null;
        }
    }

    private Integer getOptionalInteger(JsonNode node) {
        if (node == null || node.isNull() || !node.isInt()) {
            return null;
        } else {
            return node.asInt();
        }
    }

    private Boolean getOptionalBoolean(JsonNode node) {
        if (node == null || node.isNull() || !node.isBoolean()) {
            return null;
        } else {
            return node.asBoolean();
        }
    }

    private LocalTime getOptionalLocalTime(JsonNode node) {
        if (node == null || node.isNull() || !node.isTextual()) {
            return null;
        } else {
            String strValue = node.asText();
            if (strValue.matches(pattern)) {
                return LocalTime.parse(strValue, sdf);
            } else {
                return null;
            }
        }
    }


    @Override
    public StoreItem deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        String uuid = getOptionalString(node.get("uuid"));
        String city = getOptionalString(node.get("city"));
        String postalCode = getOptionalString(node.get("postalCode"));
        String street = getOptionalString(node.get("street"));
        String street2 = getOptionalString(node.get("street2"));
        String street3 = getOptionalString(node.get("street3"));
        String addressName = getOptionalString(node.get("addressName"));


        Double longitude = getOptionalDouble(node.get("longitude"));
        Double latitude = getOptionalDouble(node.get("latitude"));

        Integer complexNumber = getOptionalInteger(node.get("complexNumber"));
        Integer sapStoreID = getOptionalInteger(node.get("sapStoreID"));

        Boolean showWarningMessage = getOptionalBoolean(node.get("showWarningMessage"));
        Boolean collectionPoint = getOptionalBoolean(node.get("collectionPoint"));

        LocalTime startTime = getOptionalLocalTime(node.get("todayOpen"));
        LocalTime endTime = getOptionalLocalTime(node.get("todayClose"));
        String locationType = getOptionalString(node.get("locationType"));


        return new StoreItem(uuid, new Address(city, postalCode, street, street2, street3, addressName), new Location(longitude, latitude, locationType), startTime, endTime, complexNumber, sapStoreID, showWarningMessage, collectionPoint);

    }


}