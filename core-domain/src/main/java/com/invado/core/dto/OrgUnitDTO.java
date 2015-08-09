package com.invado.core.dto;

/**
 * Created by Nikola on 08/08/2015.
 */
public class OrgUnitDTO {
    private Integer id;
    private String customId;
    private Integer clientId;
    private String clientName;
    private String name;
    private String street;
    private String place;
    private Integer parentOrgUnitId;
    private String parentOrgUnitName;
    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getParentOrgUnitId() {
        return parentOrgUnitId;
    }

    public void setParentOrgUnitId(Integer parentOrgUnitId) {
        this.parentOrgUnitId = parentOrgUnitId;
    }

    public String getParentOrgUnitName() {
        return parentOrgUnitName;
    }

    public void setParentOrgUnitName(String parentOrgUnitName) {
        this.parentOrgUnitName = parentOrgUnitName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
