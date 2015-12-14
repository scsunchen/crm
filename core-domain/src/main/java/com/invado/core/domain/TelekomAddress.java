package com.invado.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/**
 * Created by nikola on 14.12.2015.
 */
@Embeddable
public class TelekomAddress {
    @Size(max=40, message="{BusinessPartner.Country.Size}")
    @Column(name = "t_country")
    private String country;
    @Size(max=60, message="{BusinessPartner.Place.Size}")
    @Column(name = "t_place")
    private String place;
    @Column(name = "t_place_code")
    private String placeCode;
    @Size(max=10, message="{BusinessPartner.Post.Size}")
    @Column(name = "t_post_code")
    private String postCode;
    @Size(max=60, message="{BusinessPartner.Street.Size}")
    @Column(name = "t_street")
    private String street;
    @Column(name = "t_street_code")
    private String streetCode;
    @Column(name = "t_house_number")
    private String houseNumber;
    @Column(name = "t_address_code")
    private String addressCode;


    public TelekomAddress(){}

    public TelekomAddress(String country, String place, String placeCode, String postCode, String street, String streetCode, String houseNumber, String addressCode) {
        this.country = country;
        this.place = place;
        this.placeCode = placeCode;
        this.postCode = postCode;
        this.street = street;
        this.streetCode = streetCode;
        this.houseNumber = houseNumber;
        this.addressCode = addressCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }


    @Override
    public String toString() {
        return "TelekomAddress{" +
                "country='" + country + '\'' +
                ", place='" + place + '\'' +
                ", placeCode='" + placeCode + '\'' +
                ", postCode='" + postCode + '\'' +
                ", street='" + street + '\'' +
                ", streetCode='" + streetCode + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", addressCode='" + addressCode + '\'' +
                '}';
    }
}
