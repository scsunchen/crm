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
public class AccountNotFoundException extends ApplicationException {

    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
