/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bdragan
 */
@Embeddable
public class JournalEntryPK implements Serializable{
    
    @Column(name = "company_id")
    @NotNull(message = "{JournalEntry.Client.NotNull}")
    private Integer client;
    @Column(name = "journal_entry_type_id")
    @NotNull(message = "{JournalEntry.Type.NotNull}")
    private Integer type;
    @Column(name = "number")
    @NotNull(message = "{JournalEntry.Number.NotNull}")
    @DecimalMin(value = "1", message = "{JournalEntry.Number.Min}")
    private Integer number;
    
    public JournalEntryPK() {
    }

    public JournalEntryPK(Integer client, Integer type, Integer number) {
        this.client = client;
        this.type = type;
        this.number = number;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.client);
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.number);
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
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JournalEntryPK{" + "client=" + client + ", type=" + type + ", number=" + number + '}';
    }
}
