package com.invado.core.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by NikolaB on 6/18/2015.
 */
@Entity
@Table(name = "c_device")
public class Device implements Serializable {

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
    private String articleDesc;
    private String articleCode;

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

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
