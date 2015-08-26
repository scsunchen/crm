package com.invado.core.dto;

/**
 * Created by Nikola on 14/08/2015.
 */
public class CurrencyDTO {

    private String description;
    private String ISOCode;
    private Integer ISONumber;
    private String state;
    private String currency;
    private Long version;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public Integer getISONumber() {
        return ISONumber;
    }

    public void setISONumber(Integer ISONumber) {
        this.ISONumber = ISONumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
