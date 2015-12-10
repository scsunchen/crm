package com.invado.core.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Nikola on 14/08/2015.
 */
public class DeviceDTO {
    private Integer id;
    private String customCode;
    private String articleCode;
    private String articleDescription;
    private String serialNumber;
    private Integer deviceStatusId;
    private String deviceStatusName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate creationDate;
    private String installedSoftwareVersion;
    private Long version;

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

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }


    public String getInstalledSoftwareVersion() {
        return installedSoftwareVersion;
    }

    public void setInstalledSoftwareVersion(String installedSoftwareVersion) {
        this.installedSoftwareVersion = installedSoftwareVersion;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
}
