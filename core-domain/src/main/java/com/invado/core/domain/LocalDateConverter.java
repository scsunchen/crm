/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author bdragan
 */
@Converter
@Component
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    // mapping with java.util.Calendar breaks EclipseLink
    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        System.out.println("radi konverziju 1");
        if (attribute == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, attribute.getYear());
        // avoid 0 vs 1 based months
        calendar.set(Calendar.DAY_OF_YEAR, attribute.getDayOfYear());
        return new Date(calendar.getTimeInMillis());
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        System.out.println("radi konverziju 2");
        if (dbData == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dbData);
        int year = calendar.get(Calendar.YEAR);
        // avoid 0 vs 1 based months
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        return LocalDate.ofYearDay(year, dayOfYear);
    }

}