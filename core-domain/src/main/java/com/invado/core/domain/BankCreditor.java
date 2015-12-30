/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.invado.core.dto.BankCreditorDTO;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "c_bank_creditor", schema = "devel")
@NamedQueries({
    @NamedQuery(name = BankCreditor.COUNT_ALL,
            query = "SELECT COUNT(x) FROM BankCreditor x"),
    @NamedQuery(name = BankCreditor.READ_ALL_ORDERBY_ID,
            query = "SELECT x FROM BankCreditor x ORDER BY x.id"),
    @NamedQuery(name = BankCreditor.READ_BY_NAME_ORDERBY_NAME,
            query = "SELECT x FROM BankCreditor x WHERE UPPER(x.name) LIKE :name ORDER BY x.name"),
    
})
public class BankCreditor implements Serializable {
    
    public static final String COUNT_ALL = "BankCreditor.CountAll";
    public static final String READ_ALL_ORDERBY_ID = "BankCreditor.ReadAllOrderById";
    public static final String READ_BY_NAME_ORDERBY_NAME = "BankCreditor.ReadByNameOrderByName";
    
    private static final long serialVersionUID = 1L;


    @TableGenerator(
            name = "BankaTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Banka",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BankaTab")
    @Id
    @Column(name = "id")
    //@NotNull(message = "{Bank.Id.NotNull}")
    @DecimalMin(value="1", message = "{Bank.Id.DecimalMin}")
    private Integer id;
    @NotBlank(message = "{Bank.Name.NotBlank}")
    @Size(max = 100, message = "{Bank.Name.Size}")
    @Column(name = "name")
    private String name;        
    @Size(max = 10, message = "{Bank.PostCode.Size}")
    @Column(name = "post_code")
    private String postCode;
    @Size(max = 60, message = "{Bank.Place.Size}")
    @Column(name = "place")    
    private String place;
    @Size(max = 60, message = "{Bank.Street.Size}")
    @Column(name = "street")
    private String street;
    @Size(max = 60, message = "{Bank.ContactPerson.Size}")
    @Column(name = "contact_person")
    private String contactPerson;
    @Size(max = 60, message = "{Bank.ContactFunction.Size}")
    @Column(name = "contact_function")
    private String contactFunction;
    @Size(max = 40, message = "{Bank.ContactPhone.Size}")
    @Column(name = "contact_phone")
    private String contactPhone;    
    @Size(max = 50, message = "{Bank.Account.Size}")
    @Column(name = "account")
    private String account;
    @Version
    private Long version;

    public BankCreditor() {
    }

    public BankCreditor(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContactFunction() {
        return contactFunction;
    }

    public void setContactFunction(String contactFunction) {
        this.contactFunction = contactFunction;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BankCreditorDTO getDTO(){

        BankCreditorDTO bankCreditorDTO = new BankCreditorDTO();

        bankCreditorDTO.setPostCode(this.getPostCode());
        bankCreditorDTO.setName(this.getName());
        bankCreditorDTO.setVersion(this.getVersion());
        bankCreditorDTO.setAccount(this.getAccount());
        bankCreditorDTO.setContactFunction(this.getContactFunction());
        bankCreditorDTO.setContactPerson(this.getContactPerson());
        bankCreditorDTO.setContactPhone(this.getContactPhone());
        bankCreditorDTO.setId(this.getId());
        bankCreditorDTO.setPlace(this.getPlace());
        bankCreditorDTO.setStreet(this.getStreet());

        return bankCreditorDTO;

    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BankCreditor other = (BankCreditor) obj;
        return !(this.id != other.id && (this.id == null || !this.id.equals(other.id)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }    

    @Override
    public String toString() {
        return "rs.sproduct.payroll.domain.BankCreditor{" + "id=" + id + '}';
    }

}
