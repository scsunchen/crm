package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.LocalDateConverter;
import com.invado.customer.relationship.Utils;
import com.invado.customer.relationship.service.dto.BusinessPartnerTermsDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Entity
@Table(name = "CRM_BUSINESS_TERMS", schema = "devel")
public class BusinessPartnerTerms implements Serializable{

    @TableGenerator(
            name = "BusinessTermsTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "BusinessTerms",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BusinessTermsTab")
    @Id
    private Integer id;
    @NotNull(message = "{BusinessPartnerTerms.BusinessPartner.NotNull}")
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private BusinessPartner businessPartner;
    @Column(name = "date_from")
    @NotNull(message = "{BusinessPartnerTerms.DateFrom.NotNull}")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(style = "M-")
    private LocalDate dateFrom;
    @Column(name = "end_date")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(style = "M-")
    private LocalDate endDate;
    @Column(name = "days_to_pay")
    private Integer daysToPay;
    @Column(name = "rebate")
    @NotNull(message = "{BusinessPartnerTerms.Rabate.NotNull}")
    private Integer rebate;
    @Column(name = "status")
    @NotNull(message = "{BusinessPartnerTerms.Status.NotNull}")
    private Status status;
    @Valid//FIXME : Lazy collection validation
    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "businessPartnerTerms",
            fetch = FetchType.LAZY)
    private List<BusinessPartnerTermsItem> items = new ArrayList<>();
    @Version
    private Long version;

    @Transient
    private Integer transientPartnerId;

    public BusinessPartnerTerms() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = businessPartner;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getDaysToPay() {
        return daysToPay;
    }

    public void setDaysToPay(Integer daysToPay) {
        this.daysToPay = daysToPay;
    }

    public Integer getRebate() {
        return rebate;
    }

    public void setRebate(Integer rebate) {
        this.rebate = rebate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getTransientPartnerId() {
        return transientPartnerId;
    }

    public void setTransientPartnerId(Integer transientPartnerId) {
        this.transientPartnerId = transientPartnerId;
    }

    public int getItemsSize() {
        return items.size();
    }

    public List<BusinessPartnerTermsItem> getUnmodifiableItemsSet() {
        Collections.sort(items);
        return Collections.unmodifiableList(items);
    }

    public BusinessPartnerTermsDTO getDTO() {
        BusinessPartnerTermsDTO result = new BusinessPartnerTermsDTO();
        result.setId(this.getId());
        result.setVersion(this.getVersion());
        result.setBusinessPartnerId(this.getBusinessPartner().getId());
        result.setBusinessPartnerName(this.getBusinessPartner().getName());
        result.setDateFrom(this.getDateFrom());
        result.setEndDate(this.getEndDate());
        result.setDaysToPay(this.getDaysToPay());
        result.setRebate(this.getRebate());
        result.setStatus(this.getStatus());
        result.setVersion(this.getVersion());
        return result;
    }

    public enum Status {

        INSERT,//USLOVI SU UNETI
        VERIFIED,//USLOVI SU OVERENNI
        CLOSED;//USLOVI SU ZATVORENI.

        public String getDescription() {
            switch (this) {
                case INSERT:
                    return Utils.getMessage("BusinessPartnerTerms.Insert");
                case VERIFIED:
                    return Utils.getMessage("BusinessPartnerTerms.Verified");
                case CLOSED:
                    return Utils.getMessage("BusinessPartnerTerms.Closed");
            }
            return "";
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BusinessPartnerTerms other = (BusinessPartnerTerms) obj;
        return !(this.id != other.id && (this.id == null || !this.id.equals(other.id)));
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.invado.customer.relationship.domain.BusinessPartnerRelationshipTerms{" + "id=" + id + '}';
    }
}
