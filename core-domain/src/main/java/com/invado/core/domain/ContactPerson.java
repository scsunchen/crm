/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/**
 * @author bdragan
 */
@Embeddable
public class ContactPerson implements Serializable {

    @Size(max = 60, message = "{BusinessPartner.ContactPerson.Size}")
    @Column(name = "contact_person")
    private String name;
    @Size(max = 40, message = "{BusinessPartner.ContactPersonPhone.Size}")
    @Column(name = "contact_person_phone")
    private String phone;
    @Size(max = 60, message = "{BusinessPartner.ContactPersonFunction.Size}")
    @Column(name = "contact_person_function")
    private String function;

    public ContactPerson() {
    }

    public ContactPerson(String name, String phone, String function) {
        this.name = name;
        this.phone = phone;
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "ContactPerson{" + "name=" + name + ", phone=" + phone + ", function=" + function + '}';
    }

    
}
