/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.time.LocalDate;
import static com.invado.finance.Utils.getMessage;
import org.springframework.format.annotation.DateTimeFormat;
/**
 *
 * @author Bobic Dragan
 */
public class RequestLedgerCardDTO  {

    private Integer clientID;
    private String clientName;
    private Integer orgUnitID;
    private String orgUnitName;
    private String accountNumber;
    private String partnerRegNo;
    private String partnerName;
    @DateTimeFormat(style = "M-")
    private LocalDate creditDebitRelationDateFrom;
    @DateTimeFormat(style = "M-")
    private LocalDate creditDebitRelationDateTo;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDateFrom;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDateTo;
    private Status status;
    private Type type;    
    private CurrencyTypeDTO currency;
    private String foreignCurrencyISOCode;
    
    public static enum Status  {

        OPEN_ITEMS,
        CLOSED_ITEMS,
        ALL;
        
        public String getDescription() {
            switch(this) {
                case ALL : return getMessage("LedgerCard.Label.All");
                case CLOSED_ITEMS : return getMessage("LedgerCard.Label.ClosedStatements");
                case OPEN_ITEMS : return getMessage("LedgerCard.Label.OpenStatements");
                default: return "";
            }
        }
    }

    public static enum Type  {

        CUSTOMER,
        SUPPLIER;
    }

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPartnerRegNo() {
        return partnerRegNo;
    }

    public void setPartnerRegNo(String partnerRegNo) {
        this.partnerRegNo = partnerRegNo;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    
}