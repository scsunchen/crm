/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author draganbob
 */
public class JournalEntryDTO {
    
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal balance;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal balanceDebit;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal balanceCredit;
    @DateTimeFormat(style = "M-")
    private LocalDate recordDate;
    private String typeName;
    private Integer clientId;
    private String clientName;
    private Integer typeId;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer typeNumber;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer journalEntryNumber;
    private Long numberOfItems;
    private Long version;
    private Boolean isPosted;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalanceDebit() {
        return balanceDebit;
    }

    public void setBalanceDebit(BigDecimal balanceDebit) {
        this.balanceDebit = balanceDebit;
    }

    public BigDecimal getBalanceCredit() {
        return balanceCredit;
    }

    public void setBalanceCredit(BigDecimal balanceCredit) {
        this.balanceCredit = balanceCredit;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(Integer typeNumber) {
        this.typeNumber = typeNumber;
    }

    public Integer getJournalEntryNumber() {
        return journalEntryNumber;
    }

    public void setJournalEntryNumber(Integer journalEntryNumber) {
        this.journalEntryNumber = journalEntryNumber;
    }

    public Long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(Boolean isPosted) {
        this.isPosted = isPosted;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
        
    
}
