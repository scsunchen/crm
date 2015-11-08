/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author root
 */
public class InvoicePK implements Serializable{

    private Integer client;
    private Integer orgUnit;
    private String document;

    public InvoicePK() {
    }

    public InvoicePK(Integer clientId, Integer orgUnitId, String document) {
        this.client = clientId;
        this.orgUnit = orgUnitId;
        this.document = document;
    }

    public String getDocument() {
        return document;
    }

    public Integer getOrgUnit() {
        return orgUnit;
    }
//

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.client);
        hash = 47 * hash + Objects.hashCode(this.orgUnit);
        hash = 47 * hash + Objects.hashCode(this.document);
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
        final InvoicePK other = (InvoicePK) obj;
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (!Objects.equals(this.orgUnit, other.orgUnit)) {
            return false;
        }
        return Objects.equals(this.document, other.document);
    }

    @Override
    public String toString() {
        return "InvoicePK{" + "client=" + client + ", orgUnit=" + orgUnit + ", document=" + document + '}';
    }
    

}
