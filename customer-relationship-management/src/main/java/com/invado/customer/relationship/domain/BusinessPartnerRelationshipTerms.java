package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Entity
@Table(name = "CRM_BUSINESS_TERMS")
public class BusinessPartnerRelationshipTerms {

    @Id
    private Integer id;
    @NotNull(message = "{BusinessPartnerTerms.BusinessPartner.NotNull}")
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private BusinessPartner businessPartner;
    @Column(name = "date_from")
    @NotNull(message = "{BusinessPartnerTerms.DateFrom.NotNull}")
    private Date dateFrom;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "days_to_pay")
    private Integer daysToPay;
    @Column(name = "rebate")
    @NotNull(message = "{BusinessPartnerTerms.Rabate.NotNull}")
    private Integer rebate;
    @Column(name = "status")
    @NotNull(message = "{BusinessPartnerTerms.Status.NotNull}")
    private String status;
    @Column(name = "remark")
    private String remark;
    @Valid//FIXME : Lazy collection validation
    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "businessPartnerRelationshipTerms",
            fetch = FetchType.LAZY)
    private List<BusinessPartnerRelationshipTermsItems> items = new ArrayList<>();
    @Column(name = "version")
    private Long version;


    public BusinessPartnerRelationshipTerms() {
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
