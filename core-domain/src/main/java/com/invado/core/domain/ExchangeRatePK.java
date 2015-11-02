/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Bobic Dragan
 */
public class ExchangeRatePK implements Serializable {



    private Date applicationDate;
    private String toCurrency;

    public ExchangeRatePK() {
    }

    public ExchangeRatePK(Date applicationDate, String toCurrency) {
        this.applicationDate = applicationDate;
        this.toCurrency = toCurrency;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExchangeRatePK other = (ExchangeRatePK) obj;
        if (this.applicationDate != other.applicationDate && (this.applicationDate == null || !this.applicationDate.equals(other.applicationDate))) {
            return false;
        }
        return !((this.toCurrency == null) ? (other.toCurrency != null) : !this.toCurrency.equals(other.toCurrency));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.applicationDate != null ? this.applicationDate.hashCode() : 0);
        hash = 43 * hash + (this.toCurrency != null ? this.toCurrency.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ExchangeRatePK{" + "applicationDate=" + applicationDate + ", toCurrency=" + toCurrency + '}';
    }    
    
}