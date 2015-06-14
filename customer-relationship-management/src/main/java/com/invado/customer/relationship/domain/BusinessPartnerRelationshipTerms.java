package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by NikolaB on 6/13/2015.
 */
@Entity
@Table(name = "CRM_BUSINESS_TERMS")
public class BusinessPartnerRelationshipTerms {

    @Id
    private Integer id;
    @Column(name = "partner_id")
    private BusinessPartner businessPartner;
    @Column(name = "date_from")
    private Date dateFrom;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "days_to_pay")
    private Integer daysToPay;
    @Column(name = "rebate")
    private Integer rebate;
    @Column(name = "status")
    private String status;
    @Column(name = "remrk")
    private String remark;
    @Column(name = "version")
    private Long version;



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
