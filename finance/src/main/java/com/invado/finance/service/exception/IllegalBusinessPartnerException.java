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
public class IllegalBusinessPartnerException extends ApplicationException {

    /**
     * Creates a new instance of <code>IllegalBusinessPartnerException</code> 
     * without detail message.
     */
    public IllegalBusinessPartnerException() {
    }


    /**
     * Constructs an instance of <code>IllegalBusinessPartnerException</code> 
     * with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public IllegalBusinessPartnerException(String msg) {
        super(msg);
    }


  
    
}
