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
    @Column(name = "street")
    @Size(max=60, message="{BusinessPartner.Street.Size}")
    private String street;
    @Column(name = "post_code")
    @Size(max=10, message="{BusinessPartner.Post.Size}")
    private String postCode;
    
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
    
    //************************************************************************//    
    // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//
    
    @Override
    public String toString() {
        return "Address{" + "country=" + country + ", place=" + place 
                + ", street=" + street + ", postCode=" + postCode + '}';
    }

    
}
