package com.invado.customer.relationship.service.dto;

import com.invado.core.annotations.NativeQueryResultColumn;
import com.invado.core.annotations.NativeQueryResultEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Nikola on 06/09/2015.
 */
@NativeQueryResultEntity
public class InvoicingTransactionSetDTO {

    @NativeQueryResultColumn(index=0)
    private Integer distributorId;
    @NativeQueryResultColumn(index=1)
    private String distributorName;
    @NativeQueryResultColumn(index=2)
    private Integer merchantId;
    @NativeQueryResultColumn(index=3)
    private String merchantName;
    @NativeQueryResultColumn(index=4)
    private Integer serviceId;
    @NativeQueryResultColumn(index=5)
    private String articleCode;
    @NativeQueryResultColumn(index=6)
    private String serviceDescription;
    @NativeQueryResultColumn(index=7)
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal amount;
    @NativeQueryResultColumn(index=8)
    private Integer posId;
    @NativeQueryResultColumn(index=9)
    private String posName;
    @NativeQueryResultColumn(index=10)
    private Integer treminalId;
    @NativeQueryResultColumn(index=11)
    private String terminalName;
    @NativeQueryResultColumn(index=12)
    @DateTimeFormat(style = "M-")
    private Date responseTime;
    @NativeQueryResultColumn(index=13)
    private Long transactionId;
    @NativeQueryResultColumn(index=14)
    private Integer rownum;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal totalAmount;




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

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getPosId() {
        return posId;
    }

    public void setPosId(Integer posId) {
        this.posId = posId;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public Integer getTreminalId() {
        return treminalId;
    }

    public void setTreminalId(Integer treminalId) {
        this.treminalId = treminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getRownum() {
        return rownum;
    }

    public void setRownum(Integer rownum) {
        this.rownum = rownum;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String toString(){
        return getDistributorId()+" / "+getDistributorName()+" / "+getMerchantId()+" / "+getMerchantName()+" / "+ getTerminalName();
    }
}
