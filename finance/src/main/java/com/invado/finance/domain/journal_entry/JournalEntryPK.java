/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author draganbob
 */
public class JournalEntryPK implements Serializable {

    private Integer client;
    private Integer typeId;
    private Integer number;

    public JournalEntryPK() {
    }

    public JournalEntryPK(Integer clientID,
            Integer typeID,
            Integer number) {
        this.client = clientID;
        this.typeId = typeID;
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.client);
        hash = 43 * hash + Objects.hashCode(this.typeId);
        hash = 43 * hash + Objects.hashCode(this.number);
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
        final JournalEntryPK other = (JournalEntryPK) obj;
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (!Objects.equals(this.typeId, other.typeId)) {
            return false;
        }
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JournalEntryPK{" + "client=" + client + ", typeId=" + typeId + ", number=" + number + '}';
    }
}
