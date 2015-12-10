package com.invado.core.dto;

import com.invado.core.domain.Client;

import java.math.BigDecimal;


/**
 * Created by nikola on 11.08.2015.
 */
public class ClientDTO {

    private byte[] logo;
    private String phone;
    private String townshipCode;
    private String townshipName;
    private String TIN;
    private String fax;
    private String businessActivityCode;
    private String EMail;
    private Client.Status status;
    private String statusDescription;
    private String street;
    private String postCode;
    private Client.Type type;
    private String typeDescription;
    private String companyIDNumber;
    private String country;
    private Long version;
    private Integer id;
    private String registrationNumber;
    private String name;
    private String place;
    private BigDecimal initialCapital;
    private Client.Employee employee;
    private String vatCertificateNumber;
    private Integer bankId;
    private String bankName;
    private String bankAccount;
    private Client.InovicingType inovicingType;

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTownshipCode() {
        return townshipCode;
    }

    public void setTownshipCode(String townshipCode) {
        this.townshipCode = townshipCode;
    }

    public String getTownshipName() {
        return townshipName;
    }

    public void setTownshipName(String townshipName) {
        this.townshipName = townshipName;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getBusinessActivityCode() {
        return businessActivityCode;
    }

    public void setBusinessActivityCode(String businessActivityCode) {
        this.businessActivityCode = businessActivityCode;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public Client.Status getStatus() {
        return status;
    }

    public void setStatus(Client.Status status) {
        this.status = status;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Client.Type getType() {
        return type;
    }

    public void setType(Client.Type type) {
        this.type = type;
    }

    public String getCompanyIDNumber() {
        return companyIDNumber;
    }

    public void setCompanyIDNumber(String companyIDNumber) {
        this.companyIDNumber = companyIDNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public BigDecimal getInitialCapital() {
        return initialCapital;
    }

    public void setInitialCapital(BigDecimal initialCapital) {
        this.initialCapital = initialCapital;
    }

    public Client.Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Client.Employee employee) {
        this.employee = employee;
    }

    public String getVatCertificateNumber() {
        return vatCertificateNumber;
    }

    public void setVatCertificateNumber(String vatCertificateNumber) {
        this.vatCertificateNumber = vatCertificateNumber;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public Client.InovicingType getInovicingType() {
        return inovicingType;
    }

    public void setInovicingType(Client.InovicingType inovicingType) {
        this.inovicingType = inovicingType;
    }
}
