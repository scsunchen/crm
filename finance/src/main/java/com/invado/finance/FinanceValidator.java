/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance;

import java.lang.annotation.ElementType;
import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

/**
 *
 * @author bdragan
 */
public class FinanceValidator {

    private static final Validator validator;

    static {        
        validator = Validation.byProvider(HibernateValidator.class)
        .configure()
        .messageInterpolator(
            new ResourceBundleMessageInterpolator(
                new PlatformResourceBundleLocator( "com.invado.finance.bundle.server-strings" ))
        )                
        .buildValidatorFactory()
        .getValidator();
    }

    public static Validator get() {
        return validator;
    }
}
