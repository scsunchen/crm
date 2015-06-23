package com.invado.core.domain;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by NikolaB on 6/18/2015.
 */
@Entity
public class Device {

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
    private LocalDate creationDate;
    @Column(name = "working_start_time")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate workingStartTime;
    @Column(name = "working_end_time")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate workingEndTime;
    @Column(name = "installed_software_version")
    private String installedSoftwareVersion;
    @Version
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

    public LocalDate getWorkingStartTime() {
        return workingStartTime;
    }

    public void setWorkingStartTime(LocalDate workingStartTime) {
        this.workingStartTime = workingStartTime;
    }

    public LocalDate getWorkingEndTime() {
        return workingEndTime;
    }

    public void setWorkingEndTime(LocalDate workingEndTime) {
        this.workingEndTime = workingEndTime;
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
}
