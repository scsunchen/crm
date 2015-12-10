package com.invado.core.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by Nikola on 06/12/2015.
 */
public class DeviceHolderPartnerDTO {



    private Integer id;
    private Integer deviceId;
    private String deviceCustomCode;
    private String deviceSerialNumber;
    private Integer businessPartnerId;
    private String businessPartnerName;
    @DateTimeFormat(style = "M-")
    private LocalDate startDate;
    @DateTimeFormat(style = "M-")
    private LocalDate endDate;
    private Integer refillTypeId;
    private String refillDescription;
    private Integer connectionTypeId;
    private String connectionDescription;
    private String workingStartTime;
    private String workingEndTime;
    @DateTimeFormat(style = "M-")
    private LocalDate activationDate;
    private String MSISDN;
    private Integer transactionLimit;
    private Long limitPerDay;
    private Integer limitPerMonth;
    private String ICCID;
    private Integer telekomId;
    private Integer page;
    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCustomCode() {
        return deviceCustomCode;
    }

    public void setDeviceCustomCode(String deviceCustomCode) {
        this.deviceCustomCode = deviceCustomCode;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public Integer getBusinessPartnerId() {
        return businessPartnerId;
    }

    public void setBusinessPartnerId(Integer businessPartnerId) {
        this.businessPartnerId = businessPartnerId;
    }

    public String getBusinessPartnerName() {
        return businessPartnerName;
    }

    public void setBusinessPartnerName(String businessPartnerName) {
        this.businessPartnerName = businessPartnerName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getRefillTypeId() {
        return refillTypeId;
    }

    public void setRefillTypeId(Integer refillTypeId) {
        this.refillTypeId = refillTypeId;
    }

    public String getRefillDescription() {
        return refillDescription;
    }

    public void setRefillDescription(String refillDescription) {
        this.refillDescription = refillDescription;
    }

    public Integer getConnectionTypeId() {
        return connectionTypeId;
    }

    public void setConnectionTypeId(Integer connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    public String getConnectionDescription() {
        return connectionDescription;
    }

    public void setConnectionDescription(String connectionDescription) {
        this.connectionDescription = connectionDescription;
    }

    public String getWorkingStartTime() {
        return workingStartTime;
    }

    public void setWorkingStartTime(String workingStartTime) {
        this.workingStartTime = workingStartTime;
    }

    public String getWorkingEndTime() {
        return workingEndTime;
    }

    public void setWorkingEndTime(String workingEndTime) {
        this.workingEndTime = workingEndTime;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public Integer getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(Integer transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public Long getLimitPerDay() {
        return limitPerDay;
    }

    public void setLimitPerDay(Long limitPerDay) {
        this.limitPerDay = limitPerDay;
    }

    public Integer getLimitPerMonth() {
        return limitPerMonth;
    }

    public void setLimitPerMonth(Integer limitPerMonth) {
        this.limitPerMonth = limitPerMonth;
    }

    public String getICCID() {
        return ICCID;
    }

    public void setICCID(String ICCID) {
        this.ICCID = ICCID;
    }

    public Integer getTelekomId() {
        return telekomId;
    }

    public void setTelekomId(Integer telekomId) {
        this.telekomId = telekomId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
