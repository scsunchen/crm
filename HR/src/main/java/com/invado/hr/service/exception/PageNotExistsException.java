/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.hr.service.exception;

/**
 *
 * @author Bobic Dragan
 */
public class PageNotExistsException extends ApplicationException {

    

    public PageNotExistsException(String msg) {
        super(msg);
    }

    public PageNotExistsException() {
        super();
    }
    
}
