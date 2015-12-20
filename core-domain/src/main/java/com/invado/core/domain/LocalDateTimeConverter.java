package com.invado.core.domain;

import javax.persistence.AttributeConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikola on 30/08/2015.
 */
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //calendar.set(Calendar.YEAR, attribute.getYear());
        // avoid 0 vs 1 based months
        //calendar.set(Calendar.DAY_OF_YEAR, attribute.getDayOfYear());
        int mont = attribute.getMonthValue()-1;
        calendar.set(attribute.getYear(), mont, attribute.getDayOfMonth(), attribute.getHour(), attribute.getMinute(), attribute.getSecond());
        return new Date(calendar.getTimeInMillis());
    }


    @Override
    public LocalDateTime convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dbData);
        int year = calendar.get(Calendar.YEAR);
        // avoid 0 vs 1 based months
        int month = calendar.get(Calendar.MONTH);
        int dayOfYear = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        LocalDateTime ld = LocalDateTime.of( year, ++month, dayOfYear, hourOfDay, second);
        return ld;
    }
}
