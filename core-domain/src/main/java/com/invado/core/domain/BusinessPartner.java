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

import com.invado.core.utils.Utils;
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
        @NamedQuery(name = BusinessPartner.READ_POINT_OF_SALE_BY_NAME_ORDERBY_NAME,
                query = "SELECT x FROM BusinessPartner x WHERE UPPER(x.name) LIKE :name AND x.type = 1 ORDER BY x.name"),
        @NamedQuery(name = BusinessPartner.READ_MERCHANT_BY_NAME_ORDERBY_NAME,
                query = "SELECT x FROM BusinessPartner x WHERE UPPER(x.name) LIKE :name AND x.type = 0 ORDER BY x.name"),
        @NamedQuery(name = BusinessPartner.READ_POINT_OF_SALE_BY_ID,
                query = "SELECT x FROM BusinessPartner x WHERE id LIKE :id AND x.type = 1"),
        @NamedQuery(name = BusinessPartner.READ_SERVICE_PROVIDER_BY_NAME_ORDERBY_NAME,
                query = "SELECT x FROM BusinessPartner x WHERE UPPER(x.name) LIKE :name AND x.type = 2 ORDER BY x.name"),
        @NamedQuery(name = BusinessPartner.READ_SERVICE_PROVIDER_BY_ID,
                query = "SELECT x FROM BusinessPartner x WHERE id LIKE :id AND x.type = 2"),
        @NamedQuery(name = BusinessPartner.READ_PARENT,
                query = "SELECT x FROM BusinessPartner x where x.parentBusinessPartner is null"),
        @NamedQuery(name = BusinessPartner.READBY_NAME_TYPE_ORDERBY_NAME,
                query = "SELECT x FROM BusinessPartner x where UPPER(x.name) LIKE :name and x.type = :type"),
        @NamedQuery(name = BusinessPartner.READ_BY_STATUS,
                query = "SELECT x FROM BusinessPartner x where x.status = :status order by x.name")
})
public class BusinessPartner implements Serializable {

    public static final String READ_BY_TIN = "BusinessPartner.ReadByTIN";
    public static final String READ_BY_NAME_ORDERBY_NAME = "BusinessPartner.ReadByNameOrderByName";
    public static final String READ_PARENT = "BusinessPartner.ReadParentPartners";
    public static final String READ_POINT_OF_SALE_BY_NAME_ORDERBY_NAME = "BusinessPartner.ReadPointOfSaleByName";
    public static final String READ_POINT_OF_SALE_BY_ID = "BusinessPartner.ReadPointOfSaleById";
    public static final String READ_SERVICE_PROVIDER_BY_NAME_ORDERBY_NAME = "BusinessPartner.ReadServiceProviderById";
    public static final String READ_GENERAL_NAME_ORDERBY_NAME = "BusinessPartner.ReadGeneralPartnerByName";
    public static final String READ_SERVICE_PROVIDER_BY_ID = "BusinessPartner.ReadGeneralPartnerById";
    public static final String READBY_NAME_TYPE_ORDERBY_NAME = "BusinessPartner.ReadByNameAndTypeOrderByName";
    public static final String READ_MERCHANT_BY_NAME_ORDERBY_NAME = "BusinessPartner.ReadMerchantByName";
    public static final String READ_BY_STATUS = "BusinessPartner.ReadbyPartner";

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
    @Embedded
    private TelekomAddress telekomAddress;
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
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "type")
    private Type type;
    @ManyToOne
    @JoinColumn(name = "parent_partner_id")
    private BusinessPartner parentBusinessPartner;
    @ManyToOne
    @JoinColumn(name = "pos_type_id")
    private POSType posType;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private BusinessPartnerStatus status;
    @Column(name = "TELEKOM_ID")
    private Integer telekomId;
    @Column(name = "TELEKOM_STATUS")
    private TelekomStatus telekomStatus;
    @Embedded
    private ContactPerson contactPerson;
    @Column(name = "REMARK")
    private String remark;
    @Version
    @Column(name = "version")
    private Long version;


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

    public TelekomAddress getTelekomAddress() {
        return telekomAddress;
    }

    public void setTelekomAddress(TelekomAddress telekomAddress) {
        this.telekomAddress = telekomAddress;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public POSType getPosType() {
        return posType;
    }

    public void setPosType(POSType posType) {
        this.posType = posType;
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

    public Integer getTelekomId() {
        return telekomId;
    }

    public void setTelekomId(Integer telekomId) {
        this.telekomId = telekomId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TelekomStatus getTelekomStatus() {
        return telekomStatus;
    }

    public void setTelekomStatus(TelekomStatus telekomStatus) {
        this.telekomStatus = telekomStatus;
    }

    public BusinessPartner getParentBusinessPartner() {
        return parentBusinessPartner;
    }

    public void setParentBusinessPartner(BusinessPartner parentBusinessPartner) {
        this.parentBusinessPartner = parentBusinessPartner;
    }

    public BusinessPartnerStatus getStatus() {
        return status;
    }

    public void setStatus(BusinessPartnerStatus status) {
        this.status = status;
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
        if (this.getAddress() != null) {
            businessPartnerDTO.setCountry(this.getAddress().getCountry());
            businessPartnerDTO.setPlace(this.getAddress().getPlace());
            businessPartnerDTO.setStreet(this.getAddress().getStreet());
            businessPartnerDTO.setPostCode(this.getPostCode());
            businessPartnerDTO.setHouseNumber(this.getAddress().getHouseNumber());
        }
        if (this.getTelekomAddress() != null) {
            businessPartnerDTO.settPlace(this.getTelekomAddress().getPlace());
            businessPartnerDTO.settPlaceCode(this.getTelekomAddress().getPlaceCode());
            businessPartnerDTO.settPostCode(this.getTelekomAddress().getPostCode());
            businessPartnerDTO.settStreet(this.getTelekomAddress().getStreet());
            businessPartnerDTO.settStreetCode(this.getTelekomAddress().getStreetCode());
            businessPartnerDTO.settHouseNumber(this.getTelekomAddress().getHouseNumber());
            businessPartnerDTO.settHouseNumberCode(this.getTelekomAddress().getHouseNumberCode());
            businessPartnerDTO.settAddressCode(this.getTelekomAddress().getAddressCode());
        }
        businessPartnerDTO.setCurrentAccount(this.getCurrentAccount());
        businessPartnerDTO.setName(this.getName());
        businessPartnerDTO.setRebate(this.getRebate());
        businessPartnerDTO.setVAT(this.getVAT());
        businessPartnerDTO.setName1(this.getName1());
        if (this.getContactPerson() != null) {
            businessPartnerDTO.setContactPersoneName(this.getContactPerson().getName());
            businessPartnerDTO.setContactPersonePhone(this.getContactPerson().getName());
            businessPartnerDTO.setContactPersoneFunction(this.getContactPerson().getFunction());
        }
        if (this.getParentBusinessPartner() != null) {
            businessPartnerDTO.setParentBusinessPartnerId(this.getParentBusinessPartner().getId());
            businessPartnerDTO.setParentBusinesspartnerName(this.getParentBusinessPartner().getName());
        }

        businessPartnerDTO.setType(this.getType());
        businessPartnerDTO.setTypeDescription(this.getType().getDescription());
        businessPartnerDTO.setLatitude(this.getLatitude());
        businessPartnerDTO.setLongitude(this.getLongitude());
        businessPartnerDTO.setPOStype(this.getPosType());
        if (businessPartnerDTO.getPOStype() != null) {
            businessPartnerDTO.setPosTypeId(this.getPosType().getId());
            businessPartnerDTO.setPostTypeName(this.getPosType().getDescription());
        }
        if (this.getStatus() != null){
            businessPartnerDTO.setPartnerStatusId(this.getStatus().getId());
            businessPartnerDTO.setPartnerStatusName(this.getStatus().getName());
        }
        if (this.getTelekomId() != null)
            businessPartnerDTO.setTelekomId(this.getTelekomId());
        businessPartnerDTO.setTelekomStatus(this.getTelekomStatus());
        businessPartnerDTO.setRemark(this.getRemark());


        return businessPartnerDTO;
    }


    public enum Type {

        MERCHANT,//Partner prodavac pravno lice
        POINT_OF_SALE, //Partner mesto prodaje, duži terminal
        SERVICE_PROVIDER, //Provajderi usloga
        GENERAL;

        public String getDescription() {
            switch (this) {
                case MERCHANT:
                    return Utils.getMessage("Businesspartner.Merchant");
                case POINT_OF_SALE:
                    return Utils.getMessage("BusinessPartner.PointOfSale");
                case SERVICE_PROVIDER:
                    return Utils.getMessage("BusinessPartner.ServiceProvider");
                case GENERAL:
                    return Utils.getMessage("BusinessPartner.General");
            }
            return "";
        }


        public static Type getEnum(String s) {
            if (MERCHANT.name().equals(s)) {
                return MERCHANT;
            } else if (POINT_OF_SALE.name().equals(s)) {
                return POINT_OF_SALE;
            } else if (SERVICE_PROVIDER.name().equals(s)) {
                return SERVICE_PROVIDER;
            } else if (GENERAL.name().equals(s)) {
                return GENERAL;
            }
            throw new IllegalArgumentException("No Enum specified for this string");
        }
    }


    public enum TelekomStatus {

        ACTIVE,//Partner je registrovan i aktivan
        DEACTIVATED; //Partner je deaktiviran


        public String getDescription() {
            switch (this) {
                case ACTIVE:
                    return Utils.getMessage("Businesspartner.TelekomStatus.Active");
                case DEACTIVATED:
                    return Utils.getMessage("Businesspartner.TelekomStatus.Deactivated");
            }
            return "";
        }

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