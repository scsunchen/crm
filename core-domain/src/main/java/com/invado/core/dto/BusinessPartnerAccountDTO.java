package com.invado.core.dto;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.Currency;

/**
 * Created by Nikola on 23/12/2015.
 */
public class BusinessPartnerAccountDTO {


    private Integer id;
    private String account;
    private BusinessPartner accountOwner;
    private Integer accountOwnerId;
    private String accountOwnerName;
    private BusinessPartner bank;
    private Integer bankId;
    private String bankName;
    private Currency currency;
    private String currencyISOCode;
    private String currencyDescription;
    private Long version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BusinessPartner getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(BusinessPartner accountOwner) {
        this.accountOwner = accountOwner;
    }

    public Integer getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(Integer accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    public String getAccountOwnerName() {
        return accountOwnerName;
    }

    public void setAccountOwnerName(String accountOwnerName) {
        this.accountOwnerName = accountOwnerName;
    }

    public BusinessPartner getBank() {
        return bank;
    }

    public void setBank(BusinessPartner bank) {
        this.bank = bank;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCurrencyISOCode() {
        return currencyISOCode;
    }

    public void setCurrencyISOCode(String currencyISOCode) {
        this.currencyISOCode = currencyISOCode;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
