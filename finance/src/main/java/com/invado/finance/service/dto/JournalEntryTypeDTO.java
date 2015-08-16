/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;

import org.springframework.format.annotation.NumberFormat;


/**
 * @author bdragan
 */
public class JournalEntryTypeDTO {

    private Integer clientId;
    private String clientName;
    private Integer typeId;
    private String name;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer journalEntryNumber;
    private Long version;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getJournalEntryNumber() {
        return journalEntryNumber;
    }

    public void setJournalEntryNumber(Integer journalEntryNumber) {
        this.journalEntryNumber = journalEntryNumber;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
}
