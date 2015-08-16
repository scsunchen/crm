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
public class DescriptionNotFoundException extends ApplicationException {

   
    /**
     * Constructs an instance of <code>DescriptionNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DescriptionNotFoundException(String msg) {
        super(msg);
    }
}
