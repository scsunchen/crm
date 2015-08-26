package com.invado.customer.relationship.service.dto;

/**
 * Created by nikola on 17.08.2015.
 */
public class BusinessTermsRequestDTO {

    private Integer businessPartnerId;
    private String businessPartnerName;

    public Integer getBusinessPartnerId() {
        return businessPartnerId;
    }

    public void setBusinessPartnerId(Integer businessPartnerId) {
        this.businessPartnerId = businessPartnerId;
    }

    public String getBusinessPartnerName() {
        return businessPartnerName;
    }

    public void setBusinessPartnerName(String businessPartnerName) {
        this.businessPartnerName = businessPartnerName;
    }
}
