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
public class PostedJournalEntryDeletionException extends ApplicationException {

    public PostedJournalEntryDeletionException() {
    }

    public PostedJournalEntryDeletionException(String msg) {
        super(msg);
    }

}
