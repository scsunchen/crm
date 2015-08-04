package com.invado.customer.relationship.service.dto;


import com.invado.customer.relationship.domain.BusinessPartnerRelationshipTerms;

import java.time.LocalDate;

/**
 * Created by nikola on 15.07.2015.
 */
public class BusinessPartnerRelationshipTermsDTO {


    private Integer id;
    private Integer businessPartnerId;
    private String businessPartnerName;
    private LocalDate dateFrom;
    private LocalDate endDate;
    private Integer daysToPay;
    private Integer rebate;
    private BusinessPartnerRelationshipTerms.Status status;
    private String remark;
    private Long version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getDaysToPay() {
        return daysToPay;
    }

    public void setDaysToPay(Integer daysToPay) {
        this.daysToPay = daysToPay;
    }

    public Integer getRebate() {
        return rebate;
    }

    public void setRebate(Integer rebate) {
        this.rebate = rebate;
    }

    public BusinessPartnerRelationshipTerms.Status getStatus() {
        return status;
    }

    public void setStatus(BusinessPartnerRelationshipTerms.Status status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}