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
public class ClientNotFoundException extends ApplicationException {

    /**
     * Creates a new instance of <code>ClientNotFoundExcaption</code> without
     * detail message.
     */
    public ClientNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ClientNotFoundExcaption</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ClientNotFoundException(String msg) {
        super(msg);
    }
}
