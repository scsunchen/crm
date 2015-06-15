package com.invado.masterdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Created by NikolaB on 6/10/2015.
 */
public class TestController {
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        Locale locale = LocaleContextHolder.getLocale();
        java.text.DateFormat formatter = DateFormat.getDateInstance();
        java.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormatter.
                ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);


        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    return;
                }
                try {
                    //DateTimeFormatter throws exception for 1.1.2015.(01.01.2015. is correct)
                    //and DateFormat don't.
                    Calendar cal = Calendar.getInstance();
                    Locale locale = LocaleContextHolder.getLocale();
                    cal.setTime(DateFormat.getDateInstance(DateFormat.MEDIUM, locale).parse(text.replace("\"", "").trim()));
                    setValue(LocalDate.ofYearDay(cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR)));
                } catch (Exception ex) {
                    throw new IllegalArgumentException();
                }
            }

            @Override
            public String getAsText() {
                if (getValue() == null) {
                    return "";
                }
                return dateTimeFormatter.format((LocalDate) getValue());
            }

        });
    }
}
