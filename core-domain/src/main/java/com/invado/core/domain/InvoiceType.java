/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import com.invado.core.utils.Utils;

/**
 *
 * @author root
 */
public enum InvoiceType {

    INVOICE,
    PROFORMA_INVOICE;

    public String getDescription() {
        switch (this) {
            case INVOICE  : return Utils.getMessage("InvoiceType.Invoice");
            case PROFORMA_INVOICE : return Utils.getMessage("InvoiceType.ProformaInvoice");
        }
        return "";
    }
}
