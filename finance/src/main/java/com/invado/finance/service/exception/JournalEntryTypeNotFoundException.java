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
public class JournalEntryTypeNotFoundException extends ApplicationException {


    /**
     * Constructs an instance of <code>JournalEntryTypeNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JournalEntryTypeNotFoundException(String msg) {
        super(msg);
    }
}
