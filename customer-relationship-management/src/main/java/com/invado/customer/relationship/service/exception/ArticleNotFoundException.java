/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.service.exception;

import com.invado.core.exception.ApplicationException;

/**
 *
 * @author bdragan
 */
public class ArticleNotFoundException extends ApplicationException {

    public ArticleNotFoundException(String msg) {
        super(msg);
    }
    
}
