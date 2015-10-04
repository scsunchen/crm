package com.invado.core.dto;

import com.invado.core.domain.Currency;
import com.invado.core.domain.LocalDateConverter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Convert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Nikola on 04/10/2015.
 */
public class ExchangeRateDTO {

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate applicationDate;
    private Currency toCurrency;
    private String ISOCode;
    private String currencyDescription;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal buying;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal middle;
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal selling;
    private Integer listNumber;
    private Long version;


    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public BigDecimal getBuying() {
        return buying;
    }

    public void setBuying(BigDecimal buying) {
        this.buying = buying;
    }

    public BigDecimal getMiddle() {
        return middle;
    }

    public void setMiddle(BigDecimal middle) {
        this.middle = middle;
    }

    public BigDecimal getSelling() {
        return selling;
    }

    public void setSelling(BigDecimal selling) {
        this.selling = selling;
    }

    public Integer getListNumber() {
        return listNumber;
    }

    public void setListNumber(Integer listNumber) {
        this.listNumber = listNumber;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
