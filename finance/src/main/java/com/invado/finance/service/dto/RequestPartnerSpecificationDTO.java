/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author bdragan
 */
public class RequestPartnerSpecificationDTO {
    
    private Integer clientID;
    private String clientName;
    private Integer orgUnitID;
    private String orgUnitName;
    private String accountNumber;
    private Integer partnerId;
    private String partnerName;
    @DateTimeFormat(style = "M-")
    private LocalDate creditDebitRelationDateFrom;
    @DateTimeFormat(style = "M-")
    private LocalDate creditDebitRelationDateTo;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDateFrom;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDateTo;

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getOrgUnitID() {
        return orgUnitID;
    }

    public void setOrgUnitID(Integer orgUnitID) {
        this.orgUnitID = orgUnitID;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public LocalDate getCreditDebitRelationDateFrom() {
        return creditDebitRelationDateFrom;
    }

    public void setCreditDebitRelationDateFrom(LocalDate creditDebitRelationDateFrom) {
        this.creditDebitRelationDateFrom = creditDebitRelationDateFrom;
    }

    public LocalDate getCreditDebitRelationDateTo() {
        return creditDebitRelationDateTo;
    }

    public void setCreditDebitRelationDateTo(LocalDate creditDebitRelationDateTo) {
        this.creditDebitRelationDateTo = creditDebitRelationDateTo;
    }

    public LocalDate getValueDateFrom() {
        return valueDateFrom;
    }

    public void setValueDateFrom(LocalDate valueDateFrom) {
        this.valueDateFrom = valueDateFrom;
    }

    public LocalDate getValueDateTo() {
        return valueDateTo;
    }

    public void setValueDateTo(LocalDate valueDateTo) {
        this.valueDateTo = valueDateTo;
    }
}
