package com.invado.customer.relationship.service.dto;

import com.invado.core.annotations.NativeQueryResultColumn;
import com.invado.core.annotations.NativeQueryResultEntity;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

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
    private String serviceId;
    @NativeQueryResultColumn(index=5)
    private String serviceDescription;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NativeQueryResultColumn(index=6)
    private BigDecimal amount;
    @NativeQueryResultColumn(index=7)
    private Long transactionId;
    @NativeQueryResultColumn(index=8)
    private Integer posId;
    @NativeQueryResultColumn(index=9)
    private String posName;
    @NativeQueryResultColumn(index=10)
    private Integer treminalId;
    @NativeQueryResultColumn(index=11)
    private String treminalName;
    @NativeQueryResultColumn(index=12)
    private Integer rownum;



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

    public String getTreminalName() {
        return treminalName;
    }

    public void setTreminalName(String treminalName) {
        this.treminalName = treminalName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String toString(){
        return getDistributorId()+" / "+getDistributorName()+" / "+getMerchantId()+" / "+getMerchantName()+" / "+getTreminalName();
    }
}
