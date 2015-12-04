package com.invado.masterdata.service.dto;

import com.invado.core.domain.BusinessPartner;

/**
 * Created by Nikola on 26/11/2015.
 */
public class RequestPartnerDTO {

    private Integer id;
    private String name;
    private Integer masterPartnerId;
    private String masterPartnerName;
    private BusinessPartner.Type type;
    private Integer page;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMasterPartnerId() {
        return masterPartnerId;
    }

    public void setMasterPartnerId(Integer masterPartnerId) {
        this.masterPartnerId = masterPartnerId;
    }

    public String getMasterPartnerName() {
        return masterPartnerName;
    }

    public void setMasterPartnerName(String masterPartnerName) {
        this.masterPartnerName = masterPartnerName;
    }

    public BusinessPartner.Type getType() {
        return type;
    }

    public void setType(BusinessPartner.Type type) {
        this.type = type;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
