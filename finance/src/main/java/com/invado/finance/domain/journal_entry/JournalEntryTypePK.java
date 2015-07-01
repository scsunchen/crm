 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import java.io.Serializable;

/**
 *
 * @author bdragan
 */
public class JournalEntryTypePK implements Serializable {

    private Integer id;
    private Integer client;

    public JournalEntryTypePK() {
    }

    public JournalEntryTypePK(Integer typeID, Integer clientID) {
        this.id = typeID;
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
        final JournalEntryTypePK other = (JournalEntryTypePK) obj;
        if (this.id != other.id && (this.id == null
                || !this.id.equals(other.id))) {
            return false;
        }
        if (this.client != other.client && (this.client == null
                || !this.client.equals(other.client))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.client != null ? this.client.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "JournalEntryTypePK{" + "id=" + id + ", client=" + client + '}';
    }
    
}
