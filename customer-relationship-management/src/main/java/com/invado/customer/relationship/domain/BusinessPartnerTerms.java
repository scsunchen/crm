/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Client;
import com.invado.core.domain.LocalDateConverter;
import com.invado.customer.relationship.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "CRM_BUSINESS_TERMS")
public class BusinessPartnerTerms implements Serializable {
    
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
    @JoinColumn(name = "PARTNER_ID")
    private BusinessPartner businessPartner;
    @Column(name = "DATE_FROM")
    @NotNull(message = "{BusinessPartnerTerms.DateFrom.NotNull}")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(style = "M-")
    private LocalDate dateFrom;
    @Column(name = "END_DATE")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(style = "M-")
    private LocalDate endDate;
    @Column(name = "DAYS_TO_PAY")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer daysToPay;
    @NotNull(message = "{BusinessPartnerTerms.Rabate.NotNull}")
    @NumberFormat(style = NumberFormat.Style.PERCENT)
    private BigDecimal rebate;
    @NotNull(message = "{BusinessPartnerTerms.Status.NotNull}")
    private Status status;
    @Version
    private Long version;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "terms")
    private List<BusinessPartnerTermsItem> items = new ArrayList<>();

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

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public int getItemsSize() {
        return items.size();
    }

    public boolean removeItem(BusinessPartnerTermsItem o) {
        return items.remove(o);
    }

    public boolean addItem(BusinessPartnerTermsItem e) {
        return items.add(e);
    }

    public boolean addAll(Collection<? extends BusinessPartnerTermsItem> c) {
        return items.addAll(c);
    }

    public List<BusinessPartnerTermsItem> getItems() {
        return Collections.unmodifiableList(items);
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BusinessPartnerTerms{" + "id=" + id + '}';
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
}
