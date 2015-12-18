package com.invado.core.domain;

import com.invado.core.dto.DeviceHolderPartnerDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by NikolaB on 6/18/2015.
 */
@Entity
@Table(name = "crm_device_holder_partner")
public class DeviceHolderPartner {

    @TableGenerator(
            name = "DeviceHolderTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "DeviceHolder",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DeviceHolderTab")
    @Id
    private Integer id;
    @NotNull(message = "{DeviceHolder.Device.NotNull}")
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
    @NotNull(message = "{DeviceHolder.Partner.NotNull}")
    @ManyToOne
    @JoinColumn(name = "business_partner_id")
    private BusinessPartner businessPartner;
    @Column(name = "start_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;
    @Column(name = "end_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;
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
    @Column(name = "telekom_id")
    private Integer telekomId;


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

    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = businessPartner;
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

    public Integer getTelekomId() {
        return telekomId;
    }

    public void setTelekomId(Integer telekomId) {
        this.telekomId = telekomId;
    }

    public DeviceHolderPartnerDTO getDTO() {

        DeviceHolderPartnerDTO deviceHolderPartnerDTO = new DeviceHolderPartnerDTO();

        deviceHolderPartnerDTO.setId(this.getId());
        deviceHolderPartnerDTO.setDeviceId(this.getDevice().getId());
        deviceHolderPartnerDTO.setDeviceCustomCode(this.getDevice().getCustomCode());
        deviceHolderPartnerDTO.setDeviceSerialNumber(this.getDevice().getSerialNumber());
        deviceHolderPartnerDTO.setBusinessPartnerId(this.getBusinessPartner().getId());
        deviceHolderPartnerDTO.setBusinessPartnerName(this.getBusinessPartner().getName());
        deviceHolderPartnerDTO.setStartDate(this.getStartDate());
        deviceHolderPartnerDTO.setEndDate(this.getEndDate());
        if (this.getRefillType() != null) {
            deviceHolderPartnerDTO.setRefillDescription(this.getRefillType().getDescription());
            deviceHolderPartnerDTO.setRefillTypeId(this.getRefillType().getId());
        }
        if (this.getConnectionType() != null) {

            deviceHolderPartnerDTO.setConnectionTypeId(this.getConnectionType().getId());
            deviceHolderPartnerDTO.setConnectionDescription(this.getConnectionType().getDescription());
        }
        deviceHolderPartnerDTO.setVersion(this.getVersion());
        deviceHolderPartnerDTO.setWorkingEndTime(this.getWorkingEndTime());
        deviceHolderPartnerDTO.setWorkingStartTime(this.getWorkingStartTime());
        deviceHolderPartnerDTO.setActivationDate(this.getActivationDate());
        deviceHolderPartnerDTO.setICCID(this.getICCID());
        deviceHolderPartnerDTO.setMSISDN(this.getMSISDN());
        deviceHolderPartnerDTO.setLimitPerDay(this.getLimitPerDay());
        deviceHolderPartnerDTO.setLimitPerMonth(this.getLimitPerMonth());
        deviceHolderPartnerDTO.setTransactionLimit(this.getTransactionLimit());
        deviceHolderPartnerDTO.setTelekomId(this.getTelekomId());

        return deviceHolderPartnerDTO;
    }
}
