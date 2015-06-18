package com.invado.customer.relationship.domain;

import com.invado.core.domain.BusinessPartner;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by NikolaB on 6/18/2015.
 */
public class DeviceHolderPartner {

    @Id
    private Integer Id;
    @Column(name = "device_id")
    private Device device;
    @Column(name = "business_partner_id")
    private BusinessPartner businessPartner;
    @Column(name = "startDate")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = businessPartner;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
