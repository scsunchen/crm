/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.format;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 *
 * @author bdragan
 */
public class PercentFormatter extends org.springframework.format.number.AbstractNumberFormatter {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        if(text.contains("%") == false) {
            text += "%";
        }
        return super.parse(text, locale); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected NumberFormat getNumberFormat(Locale locale) {
        DecimalFormat editFormat= (DecimalFormat) DecimalFormat.getPercentInstance(locale);
        
        return editFormat;
    }   
}
