/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import com.invado.finance.controller.LocalDateXMLAdapter;
import com.invado.finance.domain.journal_entry.AccountDetermination;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author draganbob
 */
public class JournalEntryItemDTO {

    private Integer clientId;
    private Integer typeId;
    private Integer journalEntryNumber;
    @NumberFormat
    private Integer ordinalNumber;
    private Integer unitId;
    private String unitName;
    @DateTimeFormat(style = "M-")
    private LocalDate creditDebitRelationDate;
    private String document;
    private Integer descId;
    private String descName;
    private String accountCode;
    private String accountName;
    private Integer partnerId;
    private String businessPartnerCompanyId;
    private String partnerName;
    private String internalDocument;
    @DateTimeFormat(style = "M-")
    private LocalDate valueDate;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal debit;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal credit;
    private String note;
    private String username;
    private Integer userId;
    private AccountDetermination determination;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getJournalEntryNumber() {
        return journalEntryNumber;
    }

    public void setJournalEntryNumber(Integer journalEntryNumber) {
        this.journalEntryNumber = journalEntryNumber;
    }

    public Integer getOrdinalNumber() {
        return ordinalNumber;
    }

    public void setOrdinalNumber(Integer ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public LocalDate getCreditDebitRelationDate() {
        return creditDebitRelationDate;
    }

    @XmlJavaTypeAdapter( LocalDateXMLAdapter.class )
    public void setCreditDebitRelationDate(LocalDate creditDebitRelationDate) {
        this.creditDebitRelationDate = creditDebitRelationDate;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Integer getDescId() {
        return descId;
    }

    public void setDescId(Integer descId) {
        this.descId = descId;
    }

    public String getDescName() {
        return descName;
    }

    public void setDescName(String descName) {
        this.descName = descName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public String getBusinessPartnerCompanyId() {
        return businessPartnerCompanyId;
    }

    public void setBusinessPartnerCompanyId(String businessPartnerCompanyId) {
        this.businessPartnerCompanyId = businessPartnerCompanyId;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getInternalDocument() {
        return internalDocument;
    }

    public void setInternalDocument(String internalDocument) {
        this.internalDocument = internalDocument;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    @XmlJavaTypeAdapter( LocalDateXMLAdapter.class )
    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public AccountDetermination getDetermination() {
        return determination;
    }

    public void setDetermination(AccountDetermination determination) {
        this.determination = determination;
    }
    
}
