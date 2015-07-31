/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.exception;

import com.invado.core.exception.ApplicationException;

/**
 *
 * @author Bobic Dragan
 */
public class JournalEntryAlreadyPostedException extends ApplicationException {

    public JournalEntryAlreadyPostedException() {
    }

    public JournalEntryAlreadyPostedException(String msg) {
        super(msg);
    }

}
