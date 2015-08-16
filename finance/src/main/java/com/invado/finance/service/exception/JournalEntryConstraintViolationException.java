package com.invado.finance.service.exception;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.invado.core.exception.ConstraintViolationException;
import java.util.List;

/**
 *
 * @author bdragan
 */
public class JournalEntryConstraintViolationException extends ConstraintViolationException {

    public JournalEntryConstraintViolationException(String msg) {
        super(msg);
    }

    /**
     * Kreira izvestaj o prestupu ogranicenja
     *
     * @param msg poruka o gresci
     * @param report poruke o ogranicenjima koja su narusena
     */
    public JournalEntryConstraintViolationException(String msg, 
                                                    List<String> report) {
        super(msg, report);
    }

    public String[] getReport() {
        return super.getConstraintViolation();
    }
}
