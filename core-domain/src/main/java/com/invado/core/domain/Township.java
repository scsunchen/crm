/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import com.invado.core.dto.TownshipDTO;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
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
    @Size(max = 3, min = 3, message = "{Township.Code.Size}")
    @Pattern(regexp = "^\\d*$", message = "{Township.Code.Pattern}")
    private String code;
    @Column(name = "name")
    @NotBlank(message = "{Township.Name.NotBlank}")
    @Size(max = 100, message = "{Township.Name.Size}")
    private String name;
    @Column(name = "post_code")
    @NotBlank(message = "{Township.Name.NotBlank}")
    private String postCode;
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

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public TownshipDTO getDTO() {
        TownshipDTO townshipDTO = new TownshipDTO();

        townshipDTO.setCode(this.getCode());
        townshipDTO.setName(this.getName());
        townshipDTO.setPostCode(this.getPostCode());
        townshipDTO.setVersion(this.getVersion());
        return townshipDTO;
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
        return !((this.code == null) ? (other.code != null) : !this.code.equals(other.code));
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
