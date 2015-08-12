/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import com.invado.core.dto.BusinessPartnerDTO;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bdragan
 */
@Entity
@Table(name = "c_business_partner", schema = "devel")
@NamedQueries({
        @NamedQuery(name = BusinessPartner.READ_BY_TIN,
                query = "SELECT x FROM BusinessPartner x WHERE UPPER(x.TIN) LIKE  ?1"),
        @NamedQuery(name = BusinessPartner.READ_BY_NAME_ORDERBY_NAME,
                query = "SELECT x FROM BusinessPartner x WHERE UPPER(x.name) LIKE :name ORDER BY x.name"),
        @NamedQuery(name = BusinessPartner.READ_PARENT,
                query = "SELECT x FROM BusinessPartner x where x.parentBusinessPartner is null")
})
public class BusinessPartner implements Serializable {

    public static final String READ_BY_TIN = "BusinessPartner.ReadByTIN";
    public static final String READ_BY_NAME_ORDERBY_NAME = "BusinessPartner.ReadByNameOrderByName";
    public static final String READ_PARENT = "BusinessPartner.ReadParentPartners";

    @TableGenerator(
            name = "PartnerTab",
            table = "id_generator",
            pkColumnName = "idime",
            valueColumnName = "idvrednost",
            pkColumnValue = "Partner",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PartnerTab")
    @Id
    private Integer id;
    @Column(name = "registration_number")
    @Size(max = 13, message = "{BusinessPartner.Id.Size}")
    @NotBlank(message = "{BusinessPartner.Id.NotBlank}")
    private String companyIdNumber;
    @Size(max = 100, message = "{BusinessPartner.Name.Size}")
    @NotBlank(message = "{BusinessPartner.Name.NotBlank}")
    @Column(name = "name")
    private String name;
    @Size(max = 100, message = "{BusinessPartner.Name1.Size}")
    @Column(name = "name1")
    private String name1;
    @Embedded
    private Address address;
    @Column(name = "phone")
    @Size(max = 40, message = "{BusinessPartner.Phone.Size}")
    private String phone;
    @Column(name = "fax")
    @Size(max = 40, message = "{BusinessPartner.Fax.Size}")
    private String fax;
    @Size(max = 255, message = "{BusinessPartner.EMail.Size}")
    @Column(name = "email")
    private String EMail;
    @NotBlank(message = "{BusinessPartner.TID.NotBlank}")
    @Size(max = 9, message = "{BusinessPartner.TID.Size}")
    @Column(name = "tin")
    private String TIN;
    @Size(max = 5, message = "{BusinessPartner.CurrencyDesignation.Size}")
    @Column(name = "currency_designation")
    private String currencyDesignation;
    @Size(max = 50, message = "{BusinessPartner.Account.Size}")
    @Column(name = "current_account")
    private String currentAccount;
    @Size(max = 10, message = "{BusinessPartner.BusinessActivityCode.Size}")
    @Column(name = "activity_code")
    private String activityCode;
    @DecimalMin(value = "0", message = "{BusinessPartner.Rebate.Min}")
    @Column(name = "rebate")
    private Integer rebate;//>=0
    @DecimalMin(value = "0", message = "{BusinessPartner.InterestFreeDays.Min}")
    @Column(name = "interest_free_days")
    private Integer interestFreeDays;//>=0
    @Column(name = "vat")
    private Boolean VAT;
    @ManyToOne
    @JoinColumn(name = "parent_partner_id")
    private BusinessPartner parentBusinessPartner;
    @Embedded
    private ContactPerson contactPerson;
    @Version
    @Column(name = "version")
    private Long version;


    @Transient
    private String parentCompanyIdNumber;
    @Transient
    private String parentPartnerName;

    //************************************************************************//    
    // CONSTRUCTORS //
    //************************************************************************//

    public BusinessPartner() {
    }

    public BusinessPartner(String companyIDNumber) {
        this.companyIdNumber = companyIDNumber;
    }

    public BusinessPartner(String companyIDNumber, String TIN, String name) {
        this.companyIdNumber = companyIDNumber;
        this.TIN = TIN;
        this.name = name;
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

    public String getCompanyIdNumber() {
        return companyIdNumber;
    }

    public void setCompanyIdNumber(String companyIdNumber) {
        this.companyIdNumber = companyIdNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getCurrencyDesignation() {
        return currencyDesignation;
    }

    public void setCurrencyDesignation(String currencyDesignation) {
        this.currencyDesignation = currencyDesignation;
    }

    public String getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(String currentAccount) {
        this.currentAccount = currentAccount;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Integer getRebate() {
        return rebate;
    }

    public void setRebate(Integer rebate) {
        this.rebate = rebate;
    }

    public Integer getInterestFreeDays() {
        return interestFreeDays;
    }

    public void setInterestFreeDays(Integer interestFreeDays) {
        this.interestFreeDays = interestFreeDays;
    }

    public Boolean getVAT() {
        return VAT;
    }

    public void setVAT(Boolean VAT) {
        this.VAT = VAT;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStreet() {
        if (address == null) {
            return null;
        }
        return address.getStreet();
    }

    public String getPostCode() {
        if (address == null) {
            return null;
        }
        return address.getPostCode();
    }

    public String getPlace() {
        if (address == null) {
            return null;
        }
        return address.getPlace();
    }

    public BusinessPartner getParentBusinessPartner() {
        return parentBusinessPartner;
    }

    public void setParentBusinessPartner(BusinessPartner parentBusinessPartner) {
        this.parentBusinessPartner = parentBusinessPartner;
    }

    public String getParentCompanyIdNumber() {
        return parentCompanyIdNumber;
    }

    public void setParentCompanyIdNumber(String parentCompanyIdNumber) {
        this.parentCompanyIdNumber = parentCompanyIdNumber;
    }

    public String getParentPartnerName() {
        return parentPartnerName;
    }

    public void setParentPartnerName(String parentPartnerName) {
        this.parentPartnerName = parentPartnerName;
    }


    public BusinessPartnerDTO getDTO() {

        BusinessPartnerDTO businessPartnerDTO = new BusinessPartnerDTO();

        businessPartnerDTO.setId(this.getId());
        businessPartnerDTO.setActivityCode(this.getActivityCode());
        businessPartnerDTO.setInterestFreeDays(this.getInterestFreeDays());
        businessPartnerDTO.setPhone(this.getPhone());
        businessPartnerDTO.setFax(this.getFax());
        businessPartnerDTO.setTIN(this.getTIN());
        businessPartnerDTO.setEMail(this.getEMail());
        businessPartnerDTO.setVersion(this.getVersion());
        businessPartnerDTO.setCompanyIdNumber(this.getCompanyIdNumber());
        businessPartnerDTO.setCurrencyDesignation(this.getCurrencyDesignation());
        businessPartnerDTO.setCountry(this.getAddress().getCountry());
        businessPartnerDTO.setPlace(this.getAddress().getPlace());
        businessPartnerDTO.setStreet(this.getAddress().getStreet());
        businessPartnerDTO.setPostCode(this.getPostCode());
        businessPartnerDTO.setCurrentAccount(this.getCurrentAccount());
        businessPartnerDTO.setName(this.getName());
        businessPartnerDTO.setRebate(this.getRebate());
        businessPartnerDTO.setVAT(this.getVAT());
        businessPartnerDTO.setName1(this.getName1());
        businessPartnerDTO.setContactPersoneName(this.getContactPerson().getName());
        businessPartnerDTO.setContactPersonePhone(this.getContactPerson().getName());
        businessPartnerDTO.setContactPersoneFunction(this.getContactPerson().getFunction());
        businessPartnerDTO.setParentBusinessPartnerId(this.getParentBusinessPartner().getId());
        businessPartnerDTO.setParentBusinesspartnerName(this.getParentBusinessPartner().getName());

        return businessPartnerDTO;
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
        final BusinessPartner other = (BusinessPartner) obj;
        return !((this.companyIdNumber == null) ? (other.companyIdNumber != null) :
                !this.companyIdNumber.equals(other.companyIdNumber));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.companyIdNumber != null ? this.companyIdNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "BusinessPartner{" + "companyIdNumber=" + companyIdNumber + '}';
    }
}