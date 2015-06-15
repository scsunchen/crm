/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
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
            query = "SELECT x FROM Currency x WHERE UPPER(x.ISOCode) LIKE :iso ORDER BY x.ISOCode")
})
public class Currency implements Serializable {

    public static final String COUNT_ALL = "Currency.CountAll";
    public static final String READ_ALL_ORDERBY_ISOCODE = 
            "Currency.ReadAllOrderByISOCode";
    public static final String READ_BY_ISOCODE_ORDERBY_ISOCODE = 
            "Currency.ReadByISOCodeOrderByISOCode";
    @Id
    @Column(name = "iso_code")
    @NotNull(message = "{Currency.ISOCode.NotNull}")
    @Size(min = 3, max = 3, message = "{Currency.ISOCode.Size}")
    private String ISOCode;
    @Column(name = "description")
    private String description;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Currency other = (Currency) obj;
        if ((this.ISOCode == null) ? (other.ISOCode != null) : !this.ISOCode.equals(other.ISOCode)) {
            return false;
        }
        return true;
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