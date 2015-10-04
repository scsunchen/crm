/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import com.invado.core.dto.ExchangeRateDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.swing.event.DocumentEvent;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


/**
 *
 * @author Bobic Dragan
 */
@Entity
@Table(name = "c_exchange_rate", schema = "devel")
@IdClass(ExchangeRatePK.class)
@NamedQueries({
    @NamedQuery(name = ExchangeRate.READ_ALL_BY_TO_CURRENCY, 
        query="SELECT x FROM ExchangeRate x WHERE x.toCurrency.ISOCode = :code"
    )
})
public class ExchangeRate implements Serializable {
    
    public static final String READ_ALL_BY_TO_CURRENCY = 
            "ExchangeRate.ReadAllByToCurrency";    
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "application_date")
    @NotNull(message = "{ExchangeRate.ApplicationDate.NotNull}")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate applicationDate;
    @Id
    @NotNull(message = "{ExchangeRate.ToCurrency.NotNull}")
    @ManyToOne
    @JoinColumn(name = "to_currency")
    private Currency toCurrency;
    @Column(name = "buying")
    @NotNull(message = "{ExchangeRate.Buying.NotNull}")
    @DecimalMin(value = "0", message = "{ExchangeRate.Buying.Min}")//>= 0
    @Digits(fraction=4, integer=10, message = "{ExchangeRate.Buying.Digits}")//>= 0
    private BigDecimal buying;
    @Column(name = "middle")
    @NotNull(message = "{ExchangeRate.Middle.NotNull}")
    @DecimalMin(value = "0", message = "{ExchangeRate.Middle.Min}")//>= 0
    @Digits(fraction=4, integer=10, message = "{ExchangeRate.Middle.Digits}")//>= 0
    private BigDecimal middle;
    @Column(name = "selling")
    @NotNull(message = "{ExchangeRate.Selling.NotNull}")   
    @DecimalMin(value = "0", message = "{ExchangeRate.Selling.Min}")//>= 0    
    @Digits(fraction=4, integer=10, message = "{ExchangeRate.Selling.Digits}")//>= 0
    private BigDecimal selling;
    @Column(name = "list_no")
    private Integer listNumber;
    @Version
    private Long version;

    public ExchangeRate(LocalDate applicationDate,
                        Currency toCurrency) {
        this.applicationDate = applicationDate;
        this.toCurrency = toCurrency;
    }

    public ExchangeRate() {}

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getToCurrencyISOCode() {
        return toCurrency.getISOCode();
    }

    public String getToCurrencyDescription() {
        return toCurrency.getDescription();
    }

    public BigDecimal getBuying() {
        return buying;
    }

    public void setBuying(BigDecimal buying) {
        this.buying = buying;
    }

    public Integer getListNumber() {
        return listNumber;
    }

    public void setListNumber(Integer listNumber) {
        this.listNumber = listNumber;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public ExchangeRateDTO getDTO(){

        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();

        exchangeRateDTO.setBuying(this.getBuying());
        exchangeRateDTO.setSelling(this.getSelling());
        exchangeRateDTO.setMiddle(this.getMiddle());
        exchangeRateDTO.setListNumber(this.getListNumber());
        exchangeRateDTO.setApplicationDate(this.getApplicationDate());
        exchangeRateDTO.setISOCode(this.getToCurrencyISOCode());
        exchangeRateDTO.setCurrencyDescription(this.getToCurrencyDescription());

        return exchangeRateDTO;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExchangeRate other = (ExchangeRate) obj;
        if (this.applicationDate != other.applicationDate && (this.applicationDate == null || !this.applicationDate.equals(other.applicationDate))) {
            return false;
        }
        return !(this.toCurrency != other.toCurrency && (this.toCurrency == null || !this.toCurrency.equals(other.toCurrency)));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.applicationDate != null ? this.applicationDate.hashCode() : 0);
        hash = 59 * hash + (this.toCurrency != null ? this.toCurrency.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" + "applicationDate=" + applicationDate + ", toCurrency=" 
                + toCurrency + '}';
    }

}
