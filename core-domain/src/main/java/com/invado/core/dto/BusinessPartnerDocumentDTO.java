package com.invado.core.dto;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartnerDocument;
import com.invado.core.domain.DocumentType;

import java.time.LocalDate;

/**
 * Created by Nikola on 23/12/2015.
 */
public class BusinessPartnerDocumentDTO {

    private Integer id;
    private DocumentType type;
    private Integer typeId;
    private String typeDescription;
    private BusinessPartner businessPartnerOwner;
    private Integer businessPartnerOwnerId;
    private String businessPartnerOwnerName;
    private LocalDate inputDate;
    private LocalDate validUntil;
    private BusinessPartnerDocument.DocumentStatus status;
    private Long version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public BusinessPartner getBusinessPartnerOwner() {
        return businessPartnerOwner;
    }

    public void setBusinessPartnerOwner(BusinessPartner businessPartnerOwner) {
        this.businessPartnerOwner = businessPartnerOwner;
    }

    public Integer getBusinessPartnerOwnerId() {
        return businessPartnerOwnerId;
    }

    public void setBusinessPartnerOwnerId(Integer businessPartnerOwnerId) {
        this.businessPartnerOwnerId = businessPartnerOwnerId;
    }

    public String getBusinessPartnerOwnerName() {
        return businessPartnerOwnerName;
    }

    public void setBusinessPartnerOwnerName(String businessPartnerOwnerName) {
        this.businessPartnerOwnerName = businessPartnerOwnerName;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public BusinessPartnerDocument.DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(BusinessPartnerDocument.DocumentStatus status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
