/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;

/**
 *
 * @author bdragan
 */
public class OrgUnitPK implements Serializable {

    private Integer id;
    private Integer client;

    public OrgUnitPK() {
    }

    public OrgUnitPK(Integer orgUnitID, Integer clientID) {
        this.id = orgUnitID;
        this.client = clientID;
    }

    public Integer getId() {
        return id;
    }

    public Integer getClient() {
        return client;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrgUnitPK other = (OrgUnitPK) obj;
        if (this.id != other.id && (this.id == null 
                || !this.id.equals(other.id))) {
            return false;
        }
        return !(this.client != other.client && (this.client == null
                || !this.client.equals(other.client)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 11 * hash + (this.client != null ? this.client.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OrgUnitPK{" + "id=" + id + ", client=" + client + '}';
    }

    
}
