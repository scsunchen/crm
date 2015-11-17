package com.invado.masterdata.service.dto;

import com.invado.core.domain.BusinessPartner;

/**
 * Created by Nikola on 15/11/2015.
 */
public class MasterPartnerDTO {

    private Integer masterPartnerId;
    private String masterPartnerName;
    private BusinessPartner.Type type;

    public MasterPartnerDTO(){}


    public MasterPartnerDTO(Integer masterPartnerId, String masterPartnerName, BusinessPartner.Type type){

        this.masterPartnerId = masterPartnerId;
        this.masterPartnerName = masterPartnerName;
        this.type = type;
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
}
