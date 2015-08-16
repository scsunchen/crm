/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.exception;

import com.invado.core.exception.ConstraintViolationException;
import java.util.List;

/**
 *
 * @author bdragan
 */
public class AccountConstraintViolationException extends ConstraintViolationException {

    public AccountConstraintViolationException(String msg) {
        super(msg);
    }

    public AccountConstraintViolationException(String msg, String[] constraintViolation) {
        super(msg, constraintViolation);
    }

    public AccountConstraintViolationException(String msg, List<String> constraintViolation) {
        super(msg, constraintViolation);
    }

}
