/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain;

import com.invado.finance.Utils;

/**
 *
 * @author root
 */
public enum InvoiceBusinessPartner {
    
    DOMESTIC,
    ABROAD;
    
    public String getDescription() {
        switch (this) {
            case DOMESTIC  : return Utils.getMessage("InvoiceBusinessPartner.Domestic");
            case ABROAD : return Utils.getMessage("InvoiceBusinessPartner.Abroad");
        }
        return "";
    }
}
