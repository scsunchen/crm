/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.spi;

import java.util.List;
import java.util.Map;

/**
 *
 * @author bdragan
 */
public interface ModuleServiceProviderInterface {

    String getName();

    String getPath();
    
    Map<String, List<ModuleFeature>> getFeaturesByGroupForUser(String username);
}
