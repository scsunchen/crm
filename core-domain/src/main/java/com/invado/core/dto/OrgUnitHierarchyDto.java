package com.invado.core.dto;

import java.io.Serializable;

/**
 * Created by nikola on 14.07.2015.
 */
public class OrgUnitHierarchyDto implements Serializable{
    private String id;
    private String customId;
    private String name;
    private String place;
    private String street;
    private String version;
    private String parentOrgUnitName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getParentOrgUnitName() {
        return parentOrgUnitName;
    }

    public void setParentOrgUnitName(String parentOrgUnitName) {
        this.parentOrgUnitName = parentOrgUnitName;
    }
}
