/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.format;

import java.util.HashSet;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.number.NumberFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author bdragan
 */
public class FormattingConversionServiceFactoryBean
        extends org.springframework.format.support.FormattingConversionServiceFactoryBean {

    private static final boolean jsr310Present = ClassUtils.isPresent(
            "java.time.LocalDate", DefaultFormattingConversionService.class.getClassLoader());

    private static final boolean jodaTimePresent = ClassUtils.isPresent(
            "org.joda.time.LocalDate", DefaultFormattingConversionService.class.getClassLoader());

    public FormattingConversionServiceFactoryBean() {
        setRegisterDefaultFormatters(false);
        HashSet<FormatterRegistrar> registrars = new HashSet<>();
        FormatterRegistrar registrar = (FormatterRegistry registry) -> {
            registry.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory() {

                @Override
                public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
                    return configureFormatterFrom(annotation);
                }

                @Override
                public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
                    return configureFormatterFrom(annotation);
                }

                private Formatter<Number> configureFormatterFrom(NumberFormat annotation) {
                    if (StringUtils.hasLength(annotation.pattern())) {
                        return new NumberFormatter(resolveEmbeddedValue(annotation.pattern()));
                    } else {
                        Style style = annotation.style();
                        if (style == Style.PERCENT) {
                            return new com.invado.core.format.PercentFormatter();
                        } else if (style == Style.CURRENCY) {
                            return new com.invado.core.format.CurrencyFormatter();
                        } else {
                            return new NumberFormatter();
                        }
                    }
                }
            });
        };
        registrars.add(registrar);
        if (jsr310Present) {
            // just handling JSR-310 specific date and time types
            registrars.add(new DateTimeFormatterRegistrar());
        }
        if (jodaTimePresent) {
            registrars.add(new JodaTimeFormatterRegistrar());
        } else {
            // regular DateFormat-based Date, Calendar, Long converters
            registrars.add(new DateFormatterRegistrar());
        }
        setFormatterRegistrars(registrars);
    }

}
