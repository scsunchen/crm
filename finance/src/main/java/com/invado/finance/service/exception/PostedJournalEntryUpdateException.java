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
public class PostedJournalEntryUpdateException extends ApplicationException {

    public PostedJournalEntryUpdateException() {
    }

    public PostedJournalEntryUpdateException(String msg) {
        super(msg);
    }

}