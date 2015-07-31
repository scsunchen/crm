/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.exception;

import com.invado.core.exception.ApplicationException;

/**
 *
 * @author bdragan
 */
public class PostedInvoiceException extends ApplicationException {

    /**
     * Creates a new instance of <code>PostedInvoiceExceprion</code> without
     * detail message.
     */
    public PostedInvoiceException() {
    }

    /**
     * Constructs an instance of <code>PostedInvoiceExceprion</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PostedInvoiceException(String msg) {
        super(msg);
    }
}
