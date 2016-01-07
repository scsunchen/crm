package com.invado.core.dto;

import com.invado.core.domain.Client;
import com.invado.core.domain.LocalDateConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by nikola on 16.12.2015.
 */
public class InvoicingTransactionDTO {

    private Integer id;
    private Integer ditributorId;
    private String DistributorName;
    @DateTimeFormat(style = "M-")
    private LocalDate invoicingDate;
    @DateTimeFormat(style = "M-")
    private LocalDate invoicedFrom;
    @DateTimeFormat(style = "M-")
    private LocalDate invoicedTo;
    private String displayPeriod;
    private Integer partnerId;
    private String partnerName;
    private Integer page;
    private Long version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDitributorId() {
        return ditributorId;
    }

    public void setDitributorId(Integer ditributorId) {
        this.ditributorId = ditributorId;
    }

    public String getDistributorName() {
        return DistributorName;
    }

    public void setDistributorName(String distributorName) {
        DistributorName = distributorName;
    }

    public LocalDate getInvoicingDate() {
        return invoicingDate;
    }

    public void setInvoicingDate(LocalDate invoicingDate) {
        this.invoicingDate = invoicingDate;
    }

    public LocalDate getInvoicedFrom() {
        return invoicedFrom;
    }

    public void setInvoicedFrom(LocalDate invoicedFrom) {
        this.invoicedFrom = invoicedFrom;
    }

    public LocalDate getInvoicedTo() {
        return invoicedTo;
    }

    public void setInvoicedTo(LocalDate invoicedTo) {
        this.invoicedTo = invoicedTo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getDisplayPeriod() {
        return displayPeriod;
    }

    public void setDisplayPeriod(String displayPeriod) {
        this.displayPeriod = displayPeriod;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
