/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.hr;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 *
 * @author bdragan
 */
public class MasterdataValidator {

    private static final Validator validator;

    static {        
        validator = Validation.byProvider(HibernateValidator.class)
        .configure()
        .messageInterpolator(
            new ResourceBundleMessageInterpolator(
                new PlatformResourceBundleLocator( "com.invado.masterdata.bundle.server-strings" ))
        )                
        .buildValidatorFactory()
        .getValidator();
    }

    public static Validator get() {
        return validator;
    }
}
