/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author bdragan
 */
public class Module implements ModuleServiceProviderInterface {
    
    private String path;
    private String name;
    private Boolean active;
    private final Map<String, List<ModuleFeature>> features = new HashMap<>();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean isVisibleForUser(String username) {
        return getFeaturesByGroupForUser(username).isEmpty() == false;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ModuleFeature> put(String group, String featureName, String HRef) {
        ModuleFeature feature = new ModuleFeature(featureName, HRef);
        if (features.containsKey(group)) {
            List<ModuleFeature> featureSet = features.get(group);
            featureSet.add(feature);
            return features.put(group, featureSet);
        }
        List<ModuleFeature> featureSet = new ArrayList<>();
        featureSet.add(feature);
        return features.put(group, featureSet);
    }

    @Override
    public Map<String, List<ModuleFeature>> getFeaturesByGroupForUser(String username) {
        return Collections.unmodifiableMap(features);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.path);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Module other = (Module) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Module{" + "Path=" + path + ", name=" + name + '}';
    }

    
}