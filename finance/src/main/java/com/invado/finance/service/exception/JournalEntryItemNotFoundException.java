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
public class JournalEntryItemNotFoundException extends ApplicationException {

    public JournalEntryItemNotFoundException(String msg) {
        super(msg);
    }

    public JournalEntryItemNotFoundException() {
    }
}
