package com.invado.core.dto;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.BusinessPartnerDocument;
import com.invado.core.domain.DocumentType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by Nikola on 23/12/2015.
 */
public class BusinessPartnerDocumentDTO {

    private Integer id;
    private String description;
    private DocumentType type;
    private Integer typeId;
    private String typeDescription;
    private BusinessPartner businessPartnerOwner;
    private Integer businessPartnerOwnerId;
    private String businessPartnerOwnerName;
    @DateTimeFormat(style = "M-")
    private LocalDate inputDate;
    @DateTimeFormat(style = "M-")
    private LocalDate validUntil;
    private BusinessPartnerDocument.DocumentStatus status;
    private String statusValue;
    private String statusDescription;
    private String filePath;
    private String fileName;
    private byte[] file;
    private String fileContentType;
    private Long version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
}
