/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import com.invado.finance.domain.InvoiceBusinessPartner;
import com.invado.finance.domain.InvoiceType;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author root
 */
public class InvoiceDTO  {

    private Integer clientId;
    private String clientDesc;
    private Integer orgUnitId;
    private String orgUnitDesc;
    private String document;
    private Integer partnerID;
    private String partnerName;
    @DateTimeFormat(style = "M-")
    private LocalDate invoiceDate;
    @DateTimeFormat(style = "M-")
    private LocalDate creditRelationDate;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDate;
    private Boolean recorded;
    private Boolean paid;
    private Boolean printed;
    private InvoiceType proForma;
    private InvoiceBusinessPartner partnerType;
    private Boolean isDomesticCurrency;
    private String currencyISOCode;
    private String currencyDesc;
    private String username;
    private char[] password;
    private String contractNumber;
    @DateTimeFormat(style = "M-")
    private LocalDate contractDate;
    private Integer bankID;
    private String bankName;
    private Long version;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientDesc() {
        return clientDesc;
    }

    public void setClientDesc(String clientDesc) {
        this.clientDesc = clientDesc;
    }

    public Integer getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getOrgUnitDesc() {
        return orgUnitDesc;
    }

    public void setOrgUnitDesc(String orgUnitDesc) {
        this.orgUnitDesc = orgUnitDesc;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Integer getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(Integer partnerID) {
        this.partnerID = partnerID;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getCreditRelationDate() {
        return creditRelationDate;
    }

    public void setCreditRelationDate(LocalDate creditRelationDate) {
        this.creditRelationDate = creditRelationDate;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getPrinted() {
        return printed;
    }

    public void setPrinted(Boolean printed) {
        this.printed = printed;
    }

    public InvoiceType getProForma() {
        return proForma;
    }

    public void setProForma(InvoiceType proForma) {
        this.proForma = proForma;
    }

    public InvoiceBusinessPartner getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(InvoiceBusinessPartner partnerType) {
        this.partnerType = partnerType;
    }

    public Boolean getIsDomesticCurrency() {
        return isDomesticCurrency;
    }

    public void setIsDomesticCurrency(Boolean isDomesticCurrency) {
        this.isDomesticCurrency = isDomesticCurrency;
    }

    public String getCurrencyISOCode() {
        return currencyISOCode;
    }

    public void setCurrencyISOCode(String currencyISOCode) {
        this.currencyISOCode = currencyISOCode;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    
}
