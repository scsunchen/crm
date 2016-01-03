package com.invado.core.dto;

import com.invado.core.domain.DeviceServiceProviderRegistration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Nikola on 01/01/2016.
 */
public class DeviceServiceProviderRegistrationDTO {

    private Integer id;
    private Integer deviceId;
    private String deviceCustomCode;
    private String deviceSerialNumber;
    private Integer serviceProviderId;
    private String serviceProviderName;
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
    private Integer deviceStatusId;
    private String deviceStatusName;
    private String registrationId;
    private Long version;
    private Integer page;

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

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
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


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getDeviceStatusId() {
        return deviceStatusId;
    }

    public void setDeviceStatusId(Integer deviceStatusId) {
        this.deviceStatusId = deviceStatusId;
    }

    public String getDeviceStatusName() {
        return deviceStatusName;
    }

    public void setDeviceStatusName(String deviceStatusName) {
        this.deviceStatusName = deviceStatusName;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


}
