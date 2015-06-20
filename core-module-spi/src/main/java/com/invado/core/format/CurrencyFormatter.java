/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author bdragan
 */
public class CurrencyFormatter extends org.springframework.format.number.AbstractNumberFormatter {

    @Override
    protected NumberFormat getNumberFormat(Locale locale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        df.setParseBigDecimal(true);
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        df.setPositivePrefix("");
        df.setDecimalFormatSymbols(dfs);
        return df;
    }

}
