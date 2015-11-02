/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import com.invado.core.dto.CurrencyDTO;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Bobic Dragan
 */
@Entity
@Table(name = "c_currency", schema = "devel")
@NamedQueries({
    @NamedQuery(name = Currency.COUNT_ALL,
            query = "SELECT COUNT(x) FROM Currency x"),
    @NamedQuery(name = Currency.READ_ALL_ORDERBY_ISOCODE,
            query = "SELECT x FROM Currency x ORDER BY x.ISOCode"),
    @NamedQuery(name = Currency.READ_BY_ISOCODE_ORDERBY_ISOCODE,
            query = "SELECT x FROM Currency x WHERE UPPER(x.ISOCode) LIKE :iso ORDER BY x.ISOCode"),
        @NamedQuery(name = Currency.READ_BY_NAME_ORDERBY_NAME,
                query = "SELECT x FROM Currency x WHERE UPPER(x.currency) LIKE upper(:name) ORDER BY x.ISOCode")

})
public class Currency implements Serializable {

    public static final String COUNT_ALL = "Currency.CountAll";
    public static final String READ_ALL_ORDERBY_ISOCODE = 
            "Currency.ReadAllOrderByISOCode";
    public static final String READ_BY_ISOCODE_ORDERBY_ISOCODE = 
            "Currency.ReadByISOCodeOrderByISOCode";
    public static final String READ_BY_NAME_ORDERBY_NAME =
            "Currency.ReadByName";


    @Id
    @Column(name = "iso_code")
    @NotNull(message = "{Currency.ISOCode.NotNull}")
    @Size(min = 3, max = 3, message = "{Currency.ISOCode.Size}")
    private String ISOCode;
    @Column(name = "currency_name")
    private String currency;
    @Column(name = "iso_number")
    private Integer ISONumber;
    @Column(name = "description")
    private String description;
    @Column
    private String state;
    @Version
    private Long version;

    public Currency() {
    }

    public Currency(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public CurrencyDTO getDTO(){
        CurrencyDTO currencyDTO = new CurrencyDTO();

        currencyDTO.setCurrency(this.getCurrency());
        currencyDTO.setDescription(this.getDescription());
        currencyDTO.setISOCode(this.getISOCode());
        currencyDTO.setISONumber(this.ISONumber);
        currencyDTO.setState(this.getState());
        currencyDTO.setVersion(this.getVersion());

        return currencyDTO;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Currency other = (Currency) obj;
        return !((this.ISOCode == null) ? (other.ISOCode != null) : !this.ISOCode.equals(other.ISOCode));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.ISOCode != null ? this.ISOCode.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Currency{" + "ISOCode=" + ISOCode + '}';
    }
}