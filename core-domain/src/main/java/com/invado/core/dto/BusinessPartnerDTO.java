package com.invado.core.dto;

import com.invado.core.domain.BusinessPartner;
import com.invado.core.domain.POSType;

/**
 * Created by Nikola on 12/08/2015.
 */
public class BusinessPartnerDTO {

    private Integer id;
    private String activityCode;
    private Integer interestFreeDays;
    private String phone;
    private String fax;
    private String TIN;
    private String EMail;
    private Long version;
    private String companyIdNumber;
    private String currencyDesignation;
    private String country;
    private String place;
    private String postCode;
    private String street;
    private String houseNumber;
    private String currentAccount;
    private String name;
    private Integer rebate;
    private Boolean VAT;
    private String name1;
    private String contactPersoneName;
    private String contactPersonePhone;
    private String contactPersoneFunction;
    private POSType POStype;
    private String posTypeId;
    private String postTypeName;
    private Integer parentBusinessPartnerId;
    private String parentBusinesspartnerName;
    private BusinessPartner.Type type;
    private String typeValue;
    private String typeDescription;
    private Integer longitude;
    private Integer latitude;
    private String namePattern;
    private Integer telekomId;


    public BusinessPartnerDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Integer getInterestFreeDays() {
        return interestFreeDays;
    }

    public void setInterestFreeDays(Integer interestFreeDays) {
        this.interestFreeDays = interestFreeDays;
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

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCompanyIdNumber() {
        return companyIdNumber;
    }

    public void setCompanyIdNumber(String companyIdNumber) {
        this.companyIdNumber = companyIdNumber;
    }

    public String getCurrencyDesignation() {
        return currencyDesignation;
    }

    public void setCurrencyDesignation(String currencyDesignation) {
        this.currencyDesignation = currencyDesignation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(String currentAccount) {
        this.currentAccount = currentAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRebate() {
        return rebate;
    }

    public void setRebate(Integer rebate) {
        this.rebate = rebate;
    }

    public Boolean getVAT() {
        return VAT;
    }

    public void setVAT(Boolean VAT) {
        this.VAT = VAT;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getContactPersoneName() {
        return contactPersoneName;
    }

    public void setContactPersoneName(String contactPersoneName) {
        this.contactPersoneName = contactPersoneName;
    }

    public String getContactPersonePhone() {
        return contactPersonePhone;
    }

    public void setContactPersonePhone(String contactPersonePhone) {
        this.contactPersonePhone = contactPersonePhone;
    }

    public String getContactPersoneFunction() {
        return contactPersoneFunction;
    }

    public void setContactPersoneFunction(String contactPersoneFunction) {
        this.contactPersoneFunction = contactPersoneFunction;
    }


    public BusinessPartner.Type getType() {
        return type;
    }

    public void setType(BusinessPartner.Type type) {
        this.type = type;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getParentBusinessPartnerId() {
        return parentBusinessPartnerId;
    }

    public void setParentBusinessPartnerId(Integer parentBusinessPartnerId) {
        this.parentBusinessPartnerId = parentBusinessPartnerId;
    }

    public String getParentBusinesspartnerName() {
        return parentBusinesspartnerName;
    }

    public void setParentBusinesspartnerName(String parentBusinesspartnerName) {
        this.parentBusinesspartnerName = parentBusinesspartnerName;
    }

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    public POSType getPOStype() {
        return POStype;
    }

    public void setPOStype(POSType POStype) {
        this.POStype = POStype;
    }

    public String getPosTypeId() {
        return posTypeId;
    }

    public void setPosTypeId(String posTypeId) {
        this.posTypeId = posTypeId;
    }

    public String getPostTypeName() {
        return postTypeName;
    }

    public void setPostTypeName(String postTypeName) {
        this.postTypeName = postTypeName;
    }

    public Integer getTelekomId() {
        return telekomId;
    }

    public void setTelekomId(Integer telekomId) {
        this.telekomId = telekomId;
    }

    @Override
    public String toString() {
        return "BusinessPartnerDTO{" +
                "id=" + id +
                ", activityCode='" + activityCode + '\'' +
                ", interestFreeDays=" + interestFreeDays +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", TIN='" + TIN + '\'' +
                ", EMail='" + EMail + '\'' +
                ", version=" + version +
                ", companyIdNumber='" + companyIdNumber + '\'' +
                ", currencyDesignation='" + currencyDesignation + '\'' +
                ", country='" + country + '\'' +
                ", place='" + place + '\'' +
                ", street='" + street + '\'' +
                ", postCode='" + postCode + '\'' +
                ", currentAccount='" + currentAccount + '\'' +
                ", name='" + name + '\'' +
                ", rebate=" + rebate +
                ", VAT=" + VAT +
                ", name1='" + name1 + '\'' +
                ", contactPersoneName='" + contactPersoneName + '\'' +
                ", contactPersonePhone='" + contactPersonePhone + '\'' +
                ", contactPersoneFunction='" + contactPersoneFunction + '\'' +
                ", parentBusinessPartnerId=" + parentBusinessPartnerId +
                ", type=" + type +
                ", typeValue='" + typeValue + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", parentBusinesspartnerName='" + parentBusinesspartnerName + '\'' +
                ", namePattern='" + namePattern + '\'' +
                ", telekomId=" + telekomId +
                '}';
    }
}
