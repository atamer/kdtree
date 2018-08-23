package com.jumbo.store.dto;

public class AddressDTO {

    private final String city;
    private final String postalCode;
    private final String street;
    private final String street2;
    private final String street3;
    private final String addressName;

    AddressDTO(String city, String postalCode, String street, String street2, String street3, String addressName) {
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.street2 = street2;
        this.street3 = street3;
        this.addressName = addressName;
    }

    AddressDTO(String addressName) {
        this.addressName = addressName;
        city = null;
        postalCode = null;
        street = null;
        street2 = null;
        street3 = null;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getStreet2() {
        return street2;
    }

    public String getStreet3() {
        return street3;
    }

    public String getAddressName() {
        return addressName;
    }
}
