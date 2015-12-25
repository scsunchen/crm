package com.invado.core.domain;

import com.invado.core.dto.BusinessPartnerDocumentDTO;
import com.invado.core.utils.Utils;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Nikola on 23/12/2015.
 */
@Entity
@Table(name = "c_business_partner_document")
@NamedQueries({
        @NamedQuery(name=BusinessPartnerDocument.READ_BY_TYPE, query = "SELECT x FROM BusinessPartnerDocument x where x.type = :type")
})
public class BusinessPartnerDocument {


    public static final String READ_BY_TYPE = "BusinessPartnerDocument.GetByType";

    @TableGenerator(
            name = "DocumentTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "PartnerDocument",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DocumentTab")
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private DocumentType type;
    @ManyToOne
    @JoinColumn(name = "BUSINESS_PARTNER_OWNER_ID")
    private BusinessPartner businessPartnerOwner;
    @Column(name = "INPUT_DATE")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate inputDate;
    @Column(name = "VALID_UNTIL")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate validUntil;
    @Column(name = "STATUS_ID")
    private DocumentStatus status;
    @Version
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

    public BusinessPartner getBusinessPartnerOwner() {
        return businessPartnerOwner;
    }

    public void setBusinessPartnerOwner(BusinessPartner businessPartnerOwner) {
        this.businessPartnerOwner = businessPartnerOwner;
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

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BusinessPartnerDocumentDTO getDTO(){

        BusinessPartnerDocumentDTO dto = new BusinessPartnerDocumentDTO();

        dto.setId(this.getId());
        dto.setBusinessPartnerOwner(this.getBusinessPartnerOwner());
        dto.setBusinessPartnerOwnerId(this.getBusinessPartnerOwner().getId());
        dto.setBusinessPartnerOwnerName(this.getBusinessPartnerOwner().getName());
        dto.setType(this.getType());
        dto.setTypeId(this.getType().getId());
        dto.setTypeDescription(this.getType().getDescription());
        dto.setInputDate(this.getInputDate());
        dto.setValidUntil(this.getValidUntil());
        dto.setStatus(this.getStatus());
        dto.setVersion(this.getVersion());

        return dto;
    }

    public enum DocumentStatus {

        DRAFT,
        PENDING,
        SUBMITTED,
        RECEIVED,
        INPROCESS,
        APPROVED,
        REJECTED;


        public String getDescription() {
            switch (this) {
                case DRAFT:
                    return Utils.getMessage("Businesspartner.DocumentStatus.Draft");
                case PENDING:
                    return Utils.getMessage("Businesspartner.DocumentStatus.Pending");
                case SUBMITTED:
                    return Utils.getMessage("Businesspartner.DocumentStatus.Submitted");
                case RECEIVED:
                    return Utils.getMessage("Businesspartner.DocumentStatus.Received");
                case INPROCESS:
                    return Utils.getMessage("Businesspartner.DocumentStatus.InProcess");
                case APPROVED:
                    return Utils.getMessage("Businesspartner.DocumentStatus.Approved");
                case REJECTED:
                    return Utils.getMessage("Businesspartner.DocumentStatus.Rejected");
            }
            return "";
        }

    }
}
