/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/** 
 *
 * @author bdragan
 */
public class RequestInvoiceRecordingDTO {
    
    @NotNull(message="{RecordInvoice.IllegalArgument.Client}")
    private Integer clientId;
    private String clientName;
    @NotNull(message="{RecordInvoice.IllegalArgument.OrgUnit}")
    private Integer orgUnitId;
    private String orgUnitName;
    @NotBlank(message="{RecordInvoice.IllegalArgument.Document}")
    private String document;
    @NotNull(message="{RecordInvoice.IllegalArgument.JournalEntryDate}")
    @DateTimeFormat(style = "M-")
    private LocalDate recordDate;    
    @NotNull(message="{RecordInvoice.IllegalArgument.JournalEntryNumber}")
    private Integer entryOrderType;
    private String journalEntryTypeName;
    @NumberFormat
    private Integer journalEntryTypeNumber;
    @NumberFormat
    @NotNull(message="{RecordInvoice.IllegalArgument.JournalEntryNumber}")
    private Integer journalEntryNumber;
    @NotNull(message="{RecordInvoice.IllegalArgument.Description}")
    private Integer description;
    private String descriptionName;
    private String user;

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

    public Integer getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getEntryOrderType() {
        return entryOrderType;
    }

    public void setEntryOrderType(Integer entryOrderType) {
        this.entryOrderType = entryOrderType;
    }

    public Integer getJournalEntryNumber() {
        return journalEntryNumber;
    }

    public void setJournalEntryNumber(Integer journalEntryNumber) {
        this.journalEntryNumber = journalEntryNumber;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(Integer description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getJournalEntryTypeName() {
        return journalEntryTypeName;
    }

    public void setJournalEntryTypeName(String journalEntryTypeName) {
        this.journalEntryTypeName = journalEntryTypeName;
    }

    public Integer getJournalEntryTypeNumber() {
        return journalEntryTypeNumber;
    }

    public void setJournalEntryTypeNumber(Integer journalEntryTypeNumber) {
        this.journalEntryTypeNumber = journalEntryTypeNumber;
    }

    public String getDescriptionName() {
        return descriptionName;
    }

    public void setDescriptionName(String descriptionName) {
        this.descriptionName = descriptionName;
    }
    
    
}
