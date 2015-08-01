/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 *
 * @author root
 */
@Entity
@Table( name = "r_entry_accounts_properties", schema="devel")
public class RecordInvoiceAccount implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(max=100)
    @Column(name = "key")
    private String key;
    @ManyToOne
    @JoinColumn(name = "account_id")
    @NotNull
    private Account account;

    public RecordInvoiceAccount() {
    }
    
//constructor is used for testing
    public RecordInvoiceAccount(String key, Account account) {
        this.key = key;
        this.account = account;
    }

    public String getKey() {
        return key;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (key != null ? key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecordInvoiceAccount)) {
            return false;
        }
        RecordInvoiceAccount other = (RecordInvoiceAccount) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntryAccountsProperties{" + "key=" + key + ", account=" + account + '}';
    }

    
}
