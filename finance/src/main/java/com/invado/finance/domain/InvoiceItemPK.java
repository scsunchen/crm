/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain;

import java.io.Serializable;

/**
 *
 * @author root
 */
public class InvoiceItemPK implements Serializable {

    private InvoicePK invoice;
    private Integer ordinal;

    public InvoiceItemPK() {
    }

    public InvoiceItemPK(Integer clientId, Integer orgUnitId, String document,
            Integer ordinal) {
        this.invoice = new InvoicePK(clientId,orgUnitId,document);
        this.ordinal = ordinal;
    }

    public InvoicePK getInvoice() {
        return invoice;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InvoiceItemPK other = (InvoiceItemPK) obj;
        if (this.invoice != other.invoice && (this.invoice == null || !this.invoice.equals(other.invoice))) {
            return false;
        }
        return !(this.ordinal != other.ordinal && (this.ordinal == null || !this.ordinal.equals(other.ordinal)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        hash = 67 * hash + (this.ordinal != null ? this.ordinal.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "InvoiceItemPK{" + "invoice=" + invoice + ", ordinal=" + ordinal + '}';
    }

   
}