/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.invado.core.utils.Utils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author bdragan
 */
@Entity
@Table(name = "c_client", schema = "devel")
@NamedQueries({
    @NamedQuery(name = Client.READ_BY_TOWNSHIP,
            query = "SELECT x FROM Client x WHERE x.township.code = :code"),
    @NamedQuery(name = Client.READ_BY_TIN,
            query = "SELECT x FROM Client x WHERE x.TIN =?1"),
    @NamedQuery(name = Client.READ_BY_NAME_ORDERBY_NAME,
            query = "SELECT x FROM Client x WHERE UPPER(x.name) LIKE :name ORDER BY x.name"),
    @NamedQuery(name = Client.COUNT,
            query = "SELECT COUNT(x) FROM Client x"),
    @NamedQuery(name = Client.READ_ALL_ORDERBY_ID,
            query = "SELECT x FROM Client x ORDER BY x.id")

})
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String READ_BY_TIN = "Client.ReadByTIN";
    public static final String READ_BY_NAME_ORDERBY_NAME = "Client.ReadByNameOrderByName";
    public static final String READ_BY_TOWNSHIP = "Client.ReadByTownship";
    public static final String COUNT = "Client.Count";
    public static final String READ_ALL_ORDERBY_ID = "Client.ReadAll";

    @Id
    @Column(name = "id")
    @NotNull(message = "{Company.Id.NotNull}")
    @Range(min = 1, max = 99, message = "{Company.Id.Range}")
    private Integer id;
    @NotBlank(message = "{Company.Name.NotBlank}")
    @Size(max = 100, message = "{Company.Name.Size}")
    @Column(name = "name")
    private String name;
    @Size(max = 13, message = "{Company.CompanyNumber.Size}")
    @Column(name = "company_number")
    private String companyIDNumber;
    @Size(max = 60, message = "{Company.Place.Size}")
    @Column(name = "place")
    private String place;
    @Size(max = 60, message = "{Company.Street.Size}")
    @Column(name = "street")
    private String street;
    @Size(max = 10, message = "{Company.PostCode.Size}")
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "tin")
    @Size(max = 9, message = "{Company.TID.Size}")
    @NotBlank(message = "{Company.TID.NotNull}")
    private String TIN;
    @Size(max = 40, message = "{Company.Phone.Size}")
    @Column(name = "phone")
    private String phone;
    @Size(max = 40, message = "{Company.Fax.Size}")
    @Column(name = "fax")
    private String fax;
    @Size(max = 10, message = "{Company.BAC.Size}")
    @Column(name = "activity_code")
    private String businessActivityCode;
    @Size(max = 255, message = "{Company.EMail.Size}")
    @Column(name = "email")
    private String EMail;
    @Size(max = 40, message = "{Company.Country.Size}")
    @Column(name = "country")
    private String country;
    @DecimalMin(value = "0", message = "{Company.InitialCapital.Min}")
    @Digits(integer = 13, fraction = 2, message = "{Company.InitialCapital.Digits}")
    @Column(name = "initial_capital")
    private BigDecimal initialCapital;
    @ManyToOne
    @NotNull(message = "{Company.Township.NotNull}")
    @JoinColumn(name = "township_code")
    private Township township;
    @Column(name = "status")
    private Status status;
    @Column(name = "employee")
    private Employee employee;
    @Column(name = "type")
    private Type type;
    @Column(name = "vat_certificate_number")
    @Size(max = 20, message = "{Company.VATCertificateNumber.Size}")
    private String vatCertificateNumber;//poreski broj
    @Column(name = "registration_number")
    @Size(max = 10, message = "{Company.RegistrationNumber.Size}")
    private String registrationNumber;//registarski broj preduzeca
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @Version
    private Long version;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private BankCreditor bank;
    @Column(name = "bank_account")
    @Size(max = 50, message = "{Company.BankAccount.Size}")
    private String bankAccount;

    @Transient
    private String bankCreditor;
    
    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//

    public Client() {
    }

    public Client(Integer id) {
        this.id = id;
    }

    //************************************************************************//    
    // GET/SET METHODS //
    //************************************************************************//
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

    public String getCompanyIDNumber() {
        return companyIDNumber;
    }

    public void setCompanyIDNumber(String r) {
        this.companyIDNumber = r;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String p) {
        this.place = p;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String s) {
        this.street = s;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String p) {
        this.postCode = p;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String f) {
        this.fax = f;
    }

    public String getBusinessActivityCode() {
        return businessActivityCode;
    }

    public void setBusinessActivityCode(String bac) {
        this.businessActivityCode = bac;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String mail) {
        this.EMail = mail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getInitialCapital() {
        return initialCapital;
    }

    public void setInitialCapital(BigDecimal initialCapital) {
        this.initialCapital = initialCapital;
    }

    public Township getTownship() {
        return township;
    }

    public void setTownship(Township township) {
        this.township = township;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }



    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getVatCertificateNumber() {
        return vatCertificateNumber;
    }

    public void setVatCertificateNumber(String vatCertificateNumber) {
        this.vatCertificateNumber = vatCertificateNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BankCreditor getBank() {
        return bank;
    }

    public void setBank(BankCreditor bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }


    public String getBankCreditor() {
        return bankCreditor;
    }

    public void setBankCreditor(String bankCreditor) {
        this.bankCreditor = bankCreditor;
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
        final Client other = (Client) obj;
        if (this.id != other.id && (this.id == null
                || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Client{id=" + id + '}';
    }


    public enum Employee {

        EMPLOYER,
        EMPLOYER_AND_EMPLOYEE,
        EMPLOYEE


    }

    public enum Status {

        LEGAL_ENTITY,//pravno lice
        ENTREPRENEUR,//str,szr,..
        LEGAL_ENTITY_BUDGET;//pravno lice

        public String getDescription() {
            switch (this) {
                case LEGAL_ENTITY  : return Utils.getMessage("Client.Legal_Entity");
                case ENTREPRENEUR : return Utils.getMessage("Client.Enterpreneur");
                case LEGAL_ENTITY_BUDGET : return Utils.getMessage("Client.Legal_Entity_Budget");
            }
            return "";
        }
    }

    public enum Type {

        SMALL_COMPANY,
        LARGE_COMPANY
    }
}
