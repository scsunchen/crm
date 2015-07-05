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
public class JournalEntryTypeConstraintViolationException extends ConstraintViolationException {

    public JournalEntryTypeConstraintViolationException(String msg) {
        super(msg);
    }

    public JournalEntryTypeConstraintViolationException(String msg, String[] constraintViolation) {
        super(msg, constraintViolation);
    }

    public JournalEntryTypeConstraintViolationException(String msg, List<String> constraintViolation) {
        super(msg, constraintViolation);
    }

}
