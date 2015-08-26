/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.finance.service.dto.DescDTO;
import com.invado.finance.service.dto.JournalEntryItemDTO;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "f_description", schema="devel")
@NamedQueries({
    @NamedQuery(name = Description.COUNT_ALL, 
                query = "SELECT COUNT(x) FROM Description x"),
    @NamedQuery(name = Description.READ_ALL_ORDERBY_ID, 
                query = "SELECT x FROM Description x ORDER BY x.id"),
    @NamedQuery(name = Description.READ_BY_NAME_ORDERBY_NAME, 
                query = "SELECT x FROM Description x WHERE UPPER(x.name) LIKE :name ORDER BY x.name")
})
public class Description implements Serializable {
    
    public static final String COUNT_ALL = "Description.CountAll"; 
    public static final String READ_ALL_ORDERBY_ID = "Description.ReadAllOrderById"; 
    public static final String READ_BY_NAME_ORDERBY_NAME = "Description.ReadByNameOrderByName"; 
    
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "DescriptionGenerator")
    @TableGenerator(
            name = "DescriptionGenerator",
            table = "id_generator",            
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "description",
            allocationSize = 1
    )
    private Integer id;
    @NotBlank(message = "{Desc.Name.NotBlank}")
    @Size(max = 40, message = "{Desc.Name.Size}")
    @Column(name = "desc_name")
    private String name;
    @Version
    private Long version;

    public Description() {}

    public Description(Integer id) {
        this.id = id;
    }

    public Description(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }   

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public DescDTO getDTO() {
        DescDTO dto = new DescDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setVersion(version); 
        return dto;
    }

    public void set(DescDTO dto) {
        id = dto.getId();
        name = dto.getName();
        version = dto.getVersion();
    }

    void setJournalEntryItemDTO(JournalEntryItemDTO rezultat) {
        rezultat.setDescId(id);
        rezultat.setDescName(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Description other = (Description) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "Description{" + "id=" + id + '}';
    }
    
}