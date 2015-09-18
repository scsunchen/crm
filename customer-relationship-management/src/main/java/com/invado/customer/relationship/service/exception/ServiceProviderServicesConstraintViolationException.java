/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.service.exception;

import com.invado.core.exception.ConstraintViolationException;
import java.util.List;

/**
 *
 * @author bdragan
 */
public class ServiceProviderServicesConstraintViolationException extends ConstraintViolationException{

    public ServiceProviderServicesConstraintViolationException(String msg) {
        super(msg);
    }

    public ServiceProviderServicesConstraintViolationException(String msg, 
            String[] constraintViolation) {
        super(msg, constraintViolation);
    }

    public ServiceProviderServicesConstraintViolationException(String msg, 
            List<String> constraintViolation) {
        super(msg, constraintViolation);
    }
    
}
