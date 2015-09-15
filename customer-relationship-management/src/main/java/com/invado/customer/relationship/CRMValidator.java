/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 *
 * @author bdragan
 */
public class CRMValidator {

    private static final Validator validator;

    static {        
        validator = Validation.byProvider(HibernateValidator.class)
        .configure()
        .messageInterpolator(
            new ResourceBundleMessageInterpolator(
                new PlatformResourceBundleLocator( "com.invado.customer.relationship.bundle.server-strings" ))
        )                
        .buildValidatorFactory()
        .getValidator();
    }

    public static Validator get() {
        return validator;
    }
}
