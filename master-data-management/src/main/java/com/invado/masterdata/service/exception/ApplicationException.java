/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.masterdata.service.exception;

/**
 * The ApplicationException should be reported to the client directly (i.e.,
 * unwrapped) if business method could not be completed because of illegal client
 * input.
 *
 * @author bdragan
 */
public class ApplicationException extends Exception {

    public ApplicationException() {
    }

    /**
     * Constructs an instance of <code>ApplicationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ApplicationException(String msg) {
        super(msg);
    }
}
