/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.hr.service.exception;

/**
 *
 * @author Bobic Dragan
 */
public class CurrencyNotFoundException extends ApplicationException {

    /**
     * Creates a new instance of
     * <code>CurrencyNotFoundException</code> without detail message.
     */
    public CurrencyNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>CurrencyNotFoundException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CurrencyNotFoundException(String msg) {
        super(msg);
    }
}
