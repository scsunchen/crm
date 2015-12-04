/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author draganbob
 */
@Embeddable
public class Address implements Serializable {
    
    @Size(max=40, message="{BusinessPartner.Country.Size}")
    @Column(name = "country")
    private String country;
    @Size(max=60, message="{BusinessPartner.Place.Size}")
    @Column(name = "place")
    private String place;
    @Size(max=10, message="{BusinessPartner.Post.Size}")
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "street")
    @Size(max=60, message="{BusinessPartner.Street.Size}")
    private String street;
    @Column(name = "house_number")
    private String houseNumber;


    
    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//
    
    public Address() {}

    public Address(String country, String place, String street, String postCode) {
        this.country = country;
        this.place = place;
        this.street = street;
        this.postCode = postCode;
    }

    public Address(String country, String place, String street, String postCode, String houseNumber) {
        this.country = country;
        this.place = place;
        this.street = street;
        this.postCode = postCode;
        this.houseNumber = houseNumber;
    }
    
    //************************************************************************//    
    // GET/SET METHODS //
    //************************************************************************//
    
    public String getCountry() {
        return country;
    }

    public String getPlace() {
        return place;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    //************************************************************************//
    // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//
    
    @Override
    public String toString() {
        return "Address{" + "country=" + country + ", place=" + place 
                + ", street=" + street + ", postCode=" + postCode + '}';
    }

    
}
