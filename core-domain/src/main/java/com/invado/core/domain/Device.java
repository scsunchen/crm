package com.invado.core.domain;

import com.invado.core.dto.DeviceDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by NikolaB on 6/18/2015.
 */
@Entity
@Table(name = "crm_device")
@NamedQueries({
        @NamedQuery(name = Device.READ_BY_SERIAL_NUMBER,
                query = "SELECT x FROM Device x WHERE UPPER(x.serialNumber) LIKE  :serialNumber"),
        @NamedQuery(name = Device.READ_BY_CUSTOM_CODE,
                query = "SELECT x FROM Device x WHERE UPPER(x.customCode) LIKE :name ORDER BY x.customCode"),
        @NamedQuery(name = Device.READ_BY_CUSTOM_CODE_ANASSIGNED,
                query = "SELECT x FROM Device x WHERE UPPER(x.customCode) LIKE :name and x.id not in (select y.id from DeviceHolderPartner y) ORDER BY x.customCode ")
})
public class Device implements Serializable {

    public static final String READ_BY_SERIAL_NUMBER = "Device.ReadBySerialNumber";
    public static final String READ_BY_CUSTOM_CODE = "Device.ReadByCustomCode";
    public static final String READ_BY_CUSTOM_CODE_ANASSIGNED = "Device.ReadByCustomCodeAnassigned";

    @TableGenerator(
            name = "DeviceTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Device",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DeviceTab")
    @Id
    private Integer id;
    @Column(name = "custom_code")
    private String customCode;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
    @Column(name = "serial_number")
    private String serialNumber;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private DeviceStatus status;
    @Column(name = "creation_date")
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate creationDate;
    @Column(name = "working_start_time")
    private String workingStartTime;
    @Column(name = "working_end_time")
    private String workingEndTime;
    @Column(name = "installed_software_version")
    private String installedSoftwareVersion;
    @Version
    private Long version;

    @Transient
    private String deviceStatus;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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

    public String getInstalledSoftwareVersion() {
        return installedSoftwareVersion;
    }

    public void setInstalledSoftwareVersion(String installedSoftwareVersion) {
        this.installedSoftwareVersion = installedSoftwareVersion;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DeviceDTO getDTO() {

        DeviceDTO deviceDTO = new DeviceDTO();

        deviceDTO.setId(this.getId());
        deviceDTO.setArticleCode(this.getArticle().getCode());
        deviceDTO.setArticleDescription(this.getArticle().getDescription());
        deviceDTO.setCreationDate(this.getCreationDate());
        deviceDTO.setCustomCode(this.getCustomCode());
        if (this.getStatus() != null) {
            deviceDTO.setDeviceStatusId(this.getStatus().getId());
            deviceDTO.setDeviceStatusName(this.getStatus().getName());
        }
        deviceDTO.setCreationDate(this.getCreationDate());
        deviceDTO.setInstalledSoftwareVersion(this.getInstalledSoftwareVersion());
        deviceDTO.setSerialNumber(this.getSerialNumber());
        deviceDTO.setVersion(this.getVersion());
        deviceDTO.setWorkingEndTime(this.getWorkingStartTime());
        deviceDTO.setWorkingStartTime(this.getWorkingEndTime());

        return deviceDTO;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Device other = (Device) obj;
        return !(this.id != other.id && (this.id == null
                || !this.id.equals(other.id)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Client{id=" + id + '}';
    }
}
