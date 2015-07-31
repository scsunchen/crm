/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author draganbob
 */
@Embeddable
public class JournalEntryItemPK implements java.io.Serializable {
    
    @Column(name = "journal_entry_company")
    private Integer client;
    @Column(name = "journal_entry_type")
    private Integer type;
    @Column(name = "journal_entry_number")
    private Integer number;
    @Column(name = "ordinal")
    private Integer ordinalNumber;

    public JournalEntryItemPK() {
    }

    public JournalEntryItemPK(Integer client, Integer type, Integer number, Integer ordinalNumber) {
        this.client = client;
        this.type = type;
        this.number = number;
        this.ordinalNumber = ordinalNumber;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(Integer ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.client);
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.number);
        hash = 23 * hash + Objects.hashCode(this.ordinalNumber);
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
        final JournalEntryItemPK other = (JournalEntryItemPK) obj;
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        if (!Objects.equals(this.ordinalNumber, other.ordinalNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JournalEntryItemPK{" + "client=" + client + ", type=" + type + ", number=" + number + ", ordinalNumber=" + ordinalNumber + '}';
    }
    
}
