/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import com.invado.finance.Utils;

/**
 *
 * @author root
 */
public enum VatPercent {

    GENERAL_RATE,
    LOWER_RATE;
    
    public String getDescription() {
        switch(this) {
            case GENERAL_RATE : return Utils.getMessage("VatPercent.General");
            case LOWER_RATE : return Utils.getMessage("VatPercent.Lower");
        }
        return "";
    }
}
