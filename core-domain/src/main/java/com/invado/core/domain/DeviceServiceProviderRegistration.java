package com.invado.core.domain;

import com.invado.core.dto.DeviceServiceProviderRegistrationDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by Nikola on 01/01/2016.
 */
@Entity(name = "CRM_DEVICE_SERV_PROVIDER_REG")
public class DeviceServiceProviderRegistration {

    @TableGenerator(
            name = "DeviceReg",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "DeviceReg",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DeviceReg")
    @Id
    private Integer id;
    @NotNull(message = "{DeviceHolder.Device.NotNull}")
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
    @NotNull(message = "{DeviceHolder.Partner.NotNull}")
    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private BusinessPartner serviceProvider;
    @ManyToOne
    @JoinColumn(name = "refill_type_id")
    private PrepaidRefillType refillType;
    @ManyToOne
    @JoinColumn(name = "connection_type_id")
    private ConnectionType connectionType;
    @Column(name = "working_start_time")
    private String workingStartTime;
    @Column(name = "working_end_time")
    private String workingEndTime;
    @Column(name = "activation_date")
    @Convert(converter = LocalDateConverter.class)
    //@DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate activationDate;
    @Column(name = "MSISDN")
    private String MSISDN;
    @Column(name = "transactioin_limit")
    private Integer transactionLimit;
    @Column(name = "limit_per_day")
    private Long limitPerDay;
    @Column(name = "limit_per_month")
    private Integer limitPerMonth;
    @Column(name = "ICCID")
    private String ICCID;
    @Column(name = "registration_id")
    private String registration;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private DeviceStatus deviceStatus;
    @Version
    private Long version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public BusinessPartner getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(BusinessPartner serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public PrepaidRefillType getRefillType() {
        return refillType;
    }

    public void setRefillType(PrepaidRefillType refillType) {
        this.refillType = refillType;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
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

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    public DeviceServiceProviderRegistrationDTO getDTO() {

        DeviceServiceProviderRegistrationDTO deviceServiceproviderRegDTO = new DeviceServiceProviderRegistrationDTO();

        deviceServiceproviderRegDTO.setId(this.getId());
        deviceServiceproviderRegDTO.setDeviceId(this.getDevice().getId());
        deviceServiceproviderRegDTO.setDeviceCustomCode(this.getDevice().getCustomCode());
        deviceServiceproviderRegDTO.setDeviceSerialNumber(this.getDevice().getSerialNumber());
        deviceServiceproviderRegDTO.setServiceProviderId(this.getServiceProvider().getId());
        deviceServiceproviderRegDTO.setServiceProviderName(this.getServiceProvider().getName());
        if (this.getRefillType() != null) {
            deviceServiceproviderRegDTO.setRefillDescription(this.getRefillType().getDescription());
            deviceServiceproviderRegDTO.setRefillTypeId(this.getRefillType().getId());
        }
        if (this.getConnectionType() != null) {

            deviceServiceproviderRegDTO.setConnectionTypeId(this.getConnectionType().getId());
            deviceServiceproviderRegDTO.setConnectionDescription(this.getConnectionType().getDescription());
        }
        deviceServiceproviderRegDTO.setVersion(this.getVersion());
        deviceServiceproviderRegDTO.setWorkingEndTime(this.getWorkingEndTime());
        deviceServiceproviderRegDTO.setWorkingStartTime(this.getWorkingStartTime());
        deviceServiceproviderRegDTO.setActivationDate(this.getActivationDate());
        deviceServiceproviderRegDTO.setICCID(this.getICCID());
        deviceServiceproviderRegDTO.setMSISDN(this.getMSISDN());
        deviceServiceproviderRegDTO.setLimitPerDay(this.getLimitPerDay());
        deviceServiceproviderRegDTO.setLimitPerMonth(this.getLimitPerMonth());
        deviceServiceproviderRegDTO.setTransactionLimit(this.getTransactionLimit());
        deviceServiceproviderRegDTO.setRegistrationId(this.getRegistration());
        if (this.getDeviceStatus() != null) {
            deviceServiceproviderRegDTO.setDeviceStatusId(this.getDeviceStatus().getId());
            deviceServiceproviderRegDTO.setDeviceStatusName(this.getDeviceStatus().getName());
        }
        return deviceServiceproviderRegDTO;
    }
}
