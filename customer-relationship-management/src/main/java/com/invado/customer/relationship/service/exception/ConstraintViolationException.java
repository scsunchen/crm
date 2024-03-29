/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.service.exception;

import java.util.List;

/**
 * Reports the result of constraint violations
 *
 * @author bdragan `
 */
public class ConstraintViolationException extends ApplicationException {

    private String[] constraint;

    public ConstraintViolationException(String msg) {
        super(msg);
    }

    public ConstraintViolationException(String msg, String[] constraintViolation) {
        super(msg);
        this.constraint = constraintViolation;
    }

    public ConstraintViolationException(String msg, List<String> constraintViolation) {
        super(msg);
        this.constraint = constraintViolation.toArray(new String[]{});
    }

    public String[] getConstraintViolation() {
        return constraint;
    }

    @Override
    public String getMessage() {
        String result = super.getMessage().length() > 0 ? super.getMessage() + "." : "";
        for (String violation : constraint) {
            if (violation.length() > 0) {
                result += String.format(" %s.", violation);
            }
        }
        return result;
    }
}
