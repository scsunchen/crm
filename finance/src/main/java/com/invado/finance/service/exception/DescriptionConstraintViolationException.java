/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.exception;

import java.util.List;

/**
 *
 * @author bdragan
 */
public class DescriptionConstraintViolationException extends com.invado.core.exception.ConstraintViolationException  {

    public DescriptionConstraintViolationException(String msg) {
        super(msg);
    }

    public DescriptionConstraintViolationException(String msg, String[] constraintViolation) {
        super(msg, constraintViolation);
    }

    public DescriptionConstraintViolationException(String msg, List<String> constraintViolation) {
        super(msg, constraintViolation);
    }

}
