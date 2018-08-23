package com.jumbo.store.json;

import java.util.Optional;

/*Immutable class*/
public final class Address {

    private final String city;
    private final String postalCode;
    private final String street;
    private final String street2;
    private final String street3;
    private final String addressName;

    public Address(String city, String postalCode, String street, String street2, String street3, String addressName) {
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.street2 = street2;
        this.street3 = street3;
        this.addressName = addressName;
    }

    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    public Optional<String> getPostalCode() {
        return Optional.ofNullable(postalCode);
    }

    public Optional<String> getStreet() {
        return Optional.ofNullable(street);
    }

    public Optional<String> getStreet2() {
        return Optional.ofNullable(street2);
    }

    public Optional<String> getStreet3() {
        return Optional.ofNullable(street3);
    }

    public Optional<String> getAddressName() {
        return Optional.ofNullable(addressName);
    }
}
