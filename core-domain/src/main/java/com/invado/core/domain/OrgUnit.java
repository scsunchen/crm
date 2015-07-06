/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bdragan
 */
@Entity
@Table(name = "c_org_unit", schema = "devel", uniqueConstraints=
@UniqueConstraint(columnNames = {"company_id", "custom_id"}))
@IdClass(OrgUnitPK.class)
@NamedQueries({
        // TODO : TEST
        @NamedQuery(name = OrgUnit.COUNT_BY_CLIENT,
                query = "SELECT COUNT(x) FROM OrgUnit x WHERE x.client.id=?1"),
        // TODO : TEST
        @NamedQuery(name = OrgUnit.READ_BY_CLIENT_ORDERBY_PK,
                query = "SELECT x FROM OrgUnit x WHERE x.client.id=?1 ORDER BY "
                        + "x.client.id, x.id"),
        @NamedQuery(name = OrgUnit.READ_ALL_ORDERBY_PK,
                query = "SELECT x FROM OrgUnit x ORDER BY x.client.id, x.id"),
        @NamedQuery(name = OrgUnit.READ_BY_CLIENT,
                query = "SELECT x FROM OrgUnit x JOIN x.client p WHERE p.id = ?1"),
        @NamedQuery(name = OrgUnit.READ_BY_NAME_ORDERBY_NAME,
                query = "SELECT x FROM OrgUnit x WHERE UPPER(x.name) LIKE :name ORDER BY x.name"),
        @NamedQuery(name = OrgUnit.READ_BY_NAME_AND_CUSTOM_ID,
                query = "SELECT x FROM OrgUnit x WHERE UPPER(CONCAT(x.customId, x.name)) LIKE :pattern ORDER BY x.name"),
        @NamedQuery(name = OrgUnit.COUNT_ALL, query = "SELECT COUNT(x) FROM OrgUnit x")
})
public class OrgUnit implements Serializable {

    public static final String READ_BY_NAME_ORDERBY_NAME = "OrgUnit.ReadByNameOrderByName";
    public static final String READ_BY_CLIENT = "OrgUnit.ReadByClient";
    public static final String READ_ALL_ORDERBY_PK = "OrgUnit.ReadAllOrderByPK";
    public static final String READ_BY_CLIENT_ORDERBY_PK = "OrgUnit.ReadByClientOrderByPK";
    public static final String COUNT_BY_CLIENT = "OrgUnit.CountByClient";
    public static final String COUNT_ALL = "OrgUnit.CountAll";
    public static final String READ_BY_NAME_AND_CUSTOM_ID = "OrgUnit.ReabByNameAndCustomId";


    @TableGenerator(
            name = "OrgUnitTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "OrgUnit",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrgUnitTab")
    @Id
    @Column(name = "org_unit_id")
    @NotNull(message = "{OrgUnit.Id.NotNull}")
//    @Range(min = 1, max = 99, message = "{OrgUnit.Id.Range}")
    private Integer id;
    @ManyToOne
    @Id
    @NotNull(message = "{OrgUnit.Company.NotNull}")
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Client client;
    @NotNull(message = "{OrgUnit.UserId.NotNull}")
    @Column(name = "custom_id")
    private String customId;
    @Column(name = "name")
    @NotBlank(message = "{OrgUnit.Name.NotBlank}")
    @Size(max = 40, message = "{OrgUnit.Name.Size}")
    private String name;
    @Column(name = "place")
    @Size(max = 60, message = "{OrgUnit.Place.Size}")
    private String place;
    @Column(name = "street")
    @Size(max = 60, message = "{OrgUnit.Street.Size}")
    private String street;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "client_id_parent"),
            @JoinColumn(name = "orgunit_id_parent")
    })
    private OrgUnit orgUnit;
    @Version
    @Column(name = "version")
    private Long version;

    @Transient
    private Integer parentOrgUnitId;
    private String parentOrgUnitName;


    //************************************************************************//
    // CONSTRUCTORS //
    //************************************************************************//

    public OrgUnit() {
    }

    public OrgUnit(Integer orgUnitID) {
        this.id = orgUnitID;
    }

    public OrgUnit(Client client,
                   Integer orgUnitID,
                   String customId,
                   String name,
                   String place,
                   String street,
                   OrgUnit orgUnit,
                   Long version) {
        this.client = client;
        this.id = orgUnitID;
        this.customId = customId;
        this.name = name;
        this.place = place;
        this.street = street;
        this.orgUnit = orgUnit;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public Integer getParentOrgUnitId() {
        return parentOrgUnitId;
    }

    public void setParentOrgUnitId(Integer parentOrgUnitId) {
        this.parentOrgUnitId = parentOrgUnitId;
    }

    public String getParentOrgUnitName() {
        return parentOrgUnitName;
    }

    public void setParentOrgUnitName(String parentOrgUnitName) {
        this.parentOrgUnitName = parentOrgUnitName;
    }

    //************************************************************************//
    // DELEGATE METHODS //
    //************************************************************************//

    public Integer getClientID() {
        return client.getId();
    }

    public String getClientBAC() {
        return client.getBusinessActivityCode();
    }

    public String getClientName() {
        return client.getName();
    }

    public String getClientTIN() {
        return client.getTIN();
    }

    public String getClientStreet() {
        return client.getStreet();
    }

    public String getClientPostCode() {
        return client.getPostCode();
    }

    public String getClientPlace() {
        return client.getPlace();
    }

    public String getClientCompanyIdNumber() {
        return client.getCompanyIDNumber();
    }

    public byte[] getClientLogo() {
        return client.getLogo();
    }

    public String getClientTownshipResidenceCode() {
        return client.getTownship().getCode();
    }

    public String getClientTownshipResidenceName() {
        return client.getTownship().getName();
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
        final OrgUnit other = (OrgUnit) obj;
        if (this.client != other.client && (this.client == null || !this.client.equals(other.client))) {
            return false;
        }
        return !(this.id != other.id && (this.id == null || !this.id.equals(other.id)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.client != null ? this.client.hashCode() : 0);
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OrgUnit{" + "client=" + client + ", id=" + id + '}';
    }
}