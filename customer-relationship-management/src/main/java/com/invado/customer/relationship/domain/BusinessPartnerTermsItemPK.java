/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author bdragan
 */
public class BusinessPartnerTermsItemPK implements Serializable {
    private Integer terms;
    private Integer ordinal;

    public BusinessPartnerTermsItemPK() {
    }

    public BusinessPartnerTermsItemPK(Integer terms, Integer ordinal) {
        this.terms = terms;
        this.ordinal = ordinal;
    }

    public Integer getTerms() {
        return terms;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.terms);
        hash = 17 * hash + Objects.hashCode(this.ordinal);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BusinessPartnerTermsItemPK other = (BusinessPartnerTermsItemPK) obj;
        if (!Objects.equals(this.terms, other.terms)) {
            return false;
        }
        if (!Objects.equals(this.ordinal, other.ordinal)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BusinessPartnerTermsPK{" + "terms=" + terms + ", ordinal=" + ordinal + '}';
    }
    
}
