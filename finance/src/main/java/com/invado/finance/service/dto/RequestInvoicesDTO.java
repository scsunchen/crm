/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.service.dto;


/**
 *
 * @author bdragan
 */
public class RequestInvoicesDTO {
    
    private String document;
    private Integer invoicingTransactionId;
    private Integer partnerId;
    private Integer page;
    private String partnerName;
    
    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Integer getInvoicingTransactionId() {
        return invoicingTransactionId;
    }

    public void setInvoicingTransactionId(Integer i) {
        this.invoicingTransactionId = i;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
    
}
