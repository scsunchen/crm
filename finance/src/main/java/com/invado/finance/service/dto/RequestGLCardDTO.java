/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Bobic Dragan
 */
public class RequestGLCardDTO {

    private Integer clientID;
    private String clientName;
    private Integer orgUnitID;
    private String orgUnitName;
    private String accountNumber;
    @DateTimeFormat(style = "M-")
    private LocalDate creditDebitRelationDateFrom;
    @DateTimeFormat(style = "M-")
    private LocalDate creditDebitRelationDateTo;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDateFrom;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDateTo;
    private CurrencyTypeDTO currency;
    private String foreignCurrencyISOCode; 

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getOrgUnitID() {
        return orgUnitID;
    }

    public void setOrgUnitID(Integer orgUnitID) {
        this.orgUnitID = orgUnitID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public CurrencyTypeDTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTypeDTO currency) {
        this.currency = currency;
    }

    public String getForeignCurrencyISOCode() {
        return foreignCurrencyISOCode;
    }

    public void setForeignCurrencyISOCode(String foreignCurrencyISOCode) {
        this.foreignCurrencyISOCode = foreignCurrencyISOCode;
    }
}