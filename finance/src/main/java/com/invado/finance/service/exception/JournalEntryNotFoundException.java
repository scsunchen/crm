/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.exception;

import com.invado.core.exception.ApplicationException;

/**
 *
 * @author draganbob
 */
public class JournalEntryNotFoundException extends ApplicationException {

    public JournalEntryNotFoundException(String msg) {
        super(msg);
    }

    public JournalEntryNotFoundException() {
    }
}
