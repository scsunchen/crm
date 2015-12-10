/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.dto;

import java.math.BigDecimal;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author root
 */
public class InvoiceItemDTO {
    
    private Integer clientId;
    private String clientDesc;
    private Integer unitId;
    private String invoiceDocument;
    @NumberFormat
    private Integer ordinal;
    private String articleCode;
    private String articleDesc;
    @NumberFormat(style = NumberFormat.Style.PERCENT)
    private BigDecimal VATPercent;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal netPrice;    
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#.##")
    private BigDecimal quantity;
    @NumberFormat(style = NumberFormat.Style.PERCENT)
    private BigDecimal rabatPercent;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal totalCost;
    private String username;
    private char[] password;
    private Long invoiceVersion;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal returnValue;


    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientDesc() {
        return clientDesc;
    }

    public void setClientDesc(String clientDesc) {
        this.clientDesc = clientDesc;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getInvoiceDocument() {
        return invoiceDocument;
    }

    public void setInvoiceDocument(String invoiceDocument) {
        this.invoiceDocument = invoiceDocument;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }

    public BigDecimal getVATPercent() {
        return VATPercent;
    }

    public void setVATPercent(BigDecimal VATPercent) {
        this.VATPercent = VATPercent;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRabatPercent() {
        return rabatPercent;
    }

    public void setRabatPercent(BigDecimal rabatPercent) {
        this.rabatPercent = rabatPercent;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public Long getInvoiceVersion() {
        return invoiceVersion;
    }

    public void setInvoiceVersion(Long invoiceVersion) {
        this.invoiceVersion = invoiceVersion;
    }

    public BigDecimal getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(BigDecimal returnValue) {
        this.returnValue = returnValue;
    }
}
