/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.exception;

import com.invado.core.exception.ApplicationException;

/**
 *
 * @author Vlada
 */
public class JournalEntryNotBalancedException extends ApplicationException {

    /**
     * Creates a new instance of
     * <code>JournalEntryNotBalancedException</code> without detail message.
     */
    public JournalEntryNotBalancedException() {
    }

    /**
     * Constructs an instance of
     * <code>JournalEntryNotBalancedException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public JournalEntryNotBalancedException(String poruka) {
        super(poruka);
    }

}
