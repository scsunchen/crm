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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "c_township", schema = "devel")
@NamedQueries({
    @NamedQuery(name = Township.COUNT_ALL, 
        query = "SELECT COUNT(x) FROM Township x"),
    @NamedQuery(name = Township.READ_ALL_ORDERBY_CODE, 
        query = "SELECT x FROM Township x ORDER BY x.code")
})
public class Township implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String COUNT_ALL = "Township.CountAll";
    public static final String READ_ALL_ORDERBY_CODE = "Township.ReadAllOrderByCode";
    
    @Id
    @Column(name = "code")
    @NotNull(message = "{Township.Code.NotNull}")//
    @Size(max = 3, min = 3, message = "{Township.Code.Size}")
    @Pattern(regexp = "^\\d*$", message = "{Township.Code.Pattern}")    
    private String code;
    @Column(name = "name")
    @NotBlank(message = "{Township.Name.NotBlank}")
    @Size(max=100, message="{Township.Name.Size}")
    private String name;
    @Version
    private Long version;

    public Township() {
    }

    public Township(String code) {
        this.code = code;
    }

    public Township(String code, String name, Long version) {
        this.code = code;
        this.name = name;
        this.version = version;
    }

    public Long getVersion() {
        return version;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Township other = (Township) obj;
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.code != null ? this.code.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Township{" + "code=" + code + '}';
    }
}
