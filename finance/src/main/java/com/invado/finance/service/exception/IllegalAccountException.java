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
public class IllegalAccountException extends ApplicationException{

    public IllegalAccountException() {
        super();
    }

    public IllegalAccountException(String msg) {
        super(msg);
    }

}
