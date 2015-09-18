/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.utils;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bdragan
 */
public class Utils {
    
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());
    private static final String MESSAGE_BUNDLE = "com.invado.core.bundle.strings";
    private static final PropertyResourceBundle PROPERTY_BUNDLE = getResourceBundle();

    private Utils() {
    }

    private static PropertyResourceBundle getResourceBundle() {
        try {
            return (PropertyResourceBundle) PropertyResourceBundle.getBundle(MESSAGE_BUNDLE);
        } catch (Exception io) {
            LOG.log(Level.SEVERE, "", io);
            return null;
        }
    }

    public static String getMessage(String key) {
        return PROPERTY_BUNDLE.getString(key);
    }

    public static String getMessage(String key, Object... args) {
        return MessageFormat.format(PROPERTY_BUNDLE.getString(key), args);
    }
}