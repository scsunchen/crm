package com.invado.customer.relationship.service.dto;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Nikola on 23/08/2015.
 */
public class TransactionDTO {


    private Long id;
    private String statusId;
    private Integer typeId;
    private String typeDescription;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Integer amount;
    private Integer terminalId;
    private String terminalCustomCode;
    private Integer pointOfSaleId;
    private String pointOfSaleName;
    private Integer distributorId;
    private String distributorName;
    private Integer serviceProviderId;
    private String serviceProviderName;
    @DateTimeFormat( style = "MM")
    private LocalDateTime requestTime;
    @DateTimeFormat(style = "MM")
    private LocalDateTime responseTime;
    private Boolean invoicingStatus;


    public TransactionDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalCustomCode() {
        return terminalCustomCode;
    }

    public void setTerminalCustomCode(String terminalCustomCode) {
        this.terminalCustomCode = terminalCustomCode;
    }

    public Integer getPointOfSaleId() {
        return pointOfSaleId;
    }

    public void setPointOfSaleId(Integer pointOfSaleId) {
        this.pointOfSaleId = pointOfSaleId;
    }

    public String getPointOfSaleName() {
        return pointOfSaleName;
    }

    public void setPointOfSaleName(String pointOfSaleName) {
        this.pointOfSaleName = pointOfSaleName;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
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

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public Boolean getInvoicingStatus() {
        return invoicingStatus;
    }

    public void setInvoicingStatus(Boolean invoicingStatus) {
        this.invoicingStatus = invoicingStatus;
    }
}
