/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.util.PropertyResourceBundle;

/**
 *
 * @author root
 */
public enum VatPercent {
    
    GENERAL_RATE,
    LOWER_RATE;
    
    private static final PropertyResourceBundle PROPERTY_BUNDLE = 
            (PropertyResourceBundle) PropertyResourceBundle.getBundle("com.invado.core.bundle.strings");
    
    
    public String getDescription() {
        switch(this) {
            case GENERAL_RATE : return PROPERTY_BUNDLE.getString("VatPercent.General");
            case LOWER_RATE : return PROPERTY_BUNDLE.getString("VatPercent.Lower");
        }
        return "";
    }
}
