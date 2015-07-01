//    public static final String UPDATE_TYPE_NUMBER =
//            "JournalEntryType.UpdateTypeNumber";
//    public static final String READ_TYPE_NUMBER_BY_PK =
//            "JournalEntryType.ReadTypeNumberByPK";
//    @NamedQuery(name = JournalEntryType.UPDATE_TYPE_NUMBER,
//            query = "UPDATE JournalEntryType t SET t.number = ?2 WHERE t.id = ?1 "
//            + "AND t.client.id = ?3"),
//    @NamedQuery(name = JournalEntryType.temp.getNumber(),
//            query = "SELECT x.number FROM JournalEntryType x WHERE x.id = ?1 "
//            + "AND x.client.id = ?2"),
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain.journal_entry;

import com.invado.core.domain.Client;
import com.invado.finance.service.dto.JournalEntryTypeDTO;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 * @author bdragan
 */
@Entity
@Table(name = "f_journal_entry_type")
@IdClass(JournalEntryTypePK.class)
@NamedQueries({
    // TODO : TEST 
    @NamedQuery(name = JournalEntryType.READ_BY_CLIENT_ORDERBY_PK,
            query = "SELECT x FROM JournalEntryType x WHERE x.client.id = ?1 "
            + "ORDER BY x.client.id, x.id"),
    @NamedQuery(name = JournalEntryType.READ_ALL_ORDERBY_CLIENT_AND_TYPEID,
            query = "SELECT x FROM JournalEntryType x ORDER BY x.client.id, x.id"),
    @NamedQuery(name = JournalEntryType.READ_BY_CLIENT,
            query = "SELECT x FROM JournalEntryType x JOIN x.client p "
            + "WHERE p.id = ?1"),
    @NamedQuery(name = JournalEntryType.COUNT_BY_CLIENT,
            query = "SELECT COUNT(x) FROM JournalEntryType x WHERE x.client.id = ?1"),
    @NamedQuery(name = JournalEntryType.COUNT_ALL,
            query = "SELECT COUNT(x) FROM JournalEntryType x")
    
})
public class JournalEntryType implements Serializable {

    public static final String READ_BY_CLIENT = "JournalEntryType.ReadByClient";
    public static final String READ_ALL_ORDERBY_CLIENT_AND_TYPEID = "JournalEntryOrderType.ReadAll";
    public static final String READ_BY_CLIENT_ORDERBY_PK =
            "JournalEntryOrderType.ReadByClientOrderByPK";
    public static final String COUNT_BY_CLIENT =
            "JournalEntryType.CountByClient";
    public static final String COUNT_ALL =
            "JournalEntryType.CountAll";    
    
    @Id
    @ManyToOne
    @NotNull(message = "{JournalEntryType.Company.NotNull}")
    @JoinColumn(name = "company_id",
            referencedColumnName = "id")
    private Client client;
    @Id
    @Column(name = "type_id")
    @NotNull(message = "{JournalEntryType.Id.NotNull}")
    @Range(min = 1, max = 99, message = "{JournalEntryType.Range.NotNull}")
    private Integer id;
    @NotBlank(message = "{JournalEntryType.Name.NotBlank}")
    @Size(max = 40, message = "{JournalEntryType.Name.Size}")
    @Column(name = "type_name")
    private String name;
    @Column(name = "type_number")
    @DecimalMin(value = "0", message = "{JournalEntryType.Number.Min}")
    private Integer number;
    @Version
    private Long version;

    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//
    public JournalEntryType() {
    }

    public JournalEntryType(Integer clientID, Integer typeID) {
        this.client = new Client(clientID);
        this.id = typeID;
    }

    public JournalEntryType(Integer clientID,
            Integer typeID,
            String name,
            Integer number,
            Long version) {
        this.client = new Client(clientID);
        this.id = typeID;
        this.name = name;
        this.number = number;
        this.version = version;
    }

    //************************************************************************//    
    // GET/SET METHODS //
    //************************************************************************//
    public Client getClient() {
        return client;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer brojNaloga) {
        this.number = brojNaloga;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    //************************************************************************//    
    // DELEGATE METHODS //
    //************************************************************************//
    public String getClientName() {
        return client.getName();
    }

    public Integer getClientID() {
        return client.getId();
    }

    public JournalEntryTypeDTO getDTO() {
        JournalEntryTypeDTO dto = new JournalEntryTypeDTO();
        dto.typeId = id;
        dto.journalEntryNumber = number;
        dto.name = name;
        dto.clientId = client.getId();
        dto.clientName = client.getName();
        dto.version = version;
        return dto;
    }

    public void set(JournalEntryTypeDTO dto, Client p) {
        this.id = dto.typeId;
        this.name = dto.name;
        this.number = dto.journalEntryNumber;
        this.client = p;
        this.version = dto.version;
    }

    //************************************************************************//    
    // OVERRIDEN OBJECT METHODS  //
    //************************************************************************//
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JournalEntryType other = (JournalEntryType) obj;
        if (this.client != other.client && (this.client == null || !this.client.equals(other.client))) {
            return false;
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.client != null ? this.client.hashCode() : 0);
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "JournalEntryType{" + "client=" + client + ", id=" + id + '}';
    }
}