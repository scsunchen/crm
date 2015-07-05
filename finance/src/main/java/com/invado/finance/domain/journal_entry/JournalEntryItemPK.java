/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

/**
 *
 * @author draganbob
 */
public class JournalEntryItemPK implements java.io.Serializable {

    private JournalEntryPK journalEntry;
    private Integer ordinalNumber;

    public JournalEntryItemPK() {
    }

    public JournalEntryItemPK(Integer clientId,
            Integer typeId,
            Integer number,
            Integer ordinal) {
        journalEntry = new JournalEntryPK(clientId, typeId, number);
        this.ordinalNumber = ordinal;
    }

    public Integer getJournalEntryNumber() {
        return journalEntry.getNumber();
    }

    public Integer getJournalEntryTypeId() {
        return journalEntry.getTypeId();
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public Integer getClient() {
        return journalEntry.getClient();
    }

    public JournalEntryPK getJournalEntry() {
        return journalEntry;
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
        if (this.journalEntry != other.journalEntry && (this.journalEntry == null || !this.journalEntry.equals(other.journalEntry))) {
            return false;
        }
        if (this.ordinalNumber != other.ordinalNumber && (this.ordinalNumber == null || !this.ordinalNumber.equals(other.ordinalNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.journalEntry != null ? this.journalEntry.hashCode() : 0);
        hash = 61 * hash + (this.ordinalNumber != null ? this.ordinalNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "JournalEntryItemPK{" + "journalEntry=" + journalEntry + ", ordinalNumber=" + ordinalNumber + '}';
    }

    
    
}
