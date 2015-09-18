/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.service;

import org.junit.Test;

/**
 *
 * @author bdragan
 */
public class ServiceProviderServiceTest {
    
    @Test
    public void shouldNotThrowMissingResourceException() throws Exception {
        for (ServiceProviderService.Messages message : ServiceProviderService.Messages.values()) {
           message.get();            
        }
    }
    
}
