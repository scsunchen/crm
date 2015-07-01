/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import java.io.Serializable;

/**
 *
 * @author draganbob
 */
public class JournalEntryPK implements Serializable {

    private JournalEntryTypePK type;
    private Integer number;

    public JournalEntryPK() {
    }

    public JournalEntryPK(Integer clientID,
            Integer typeID,
            Integer number) {
        this.type = new JournalEntryTypePK(typeID, clientID);
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public JournalEntryTypePK getType() {
        return type;
    }

    public Integer getTypeID() {
        return type.getId();
    }

    public Integer getCompanyID() {
        return type.getClient();
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
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
            return false;
        }
        if (this.number != other.number && (this.number == null || !this.number.equals(other.number))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 67 * hash + (this.number != null ? this.number.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "JournalEntryPK{" + "type=" + type + ", number=" + number + '}';
    }

    
}
