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
public class JournalEntryExistsException extends ApplicationException {

    public JournalEntryExistsException(String msg) {
        super(msg);
    }

    public JournalEntryExistsException() {
    }
}
