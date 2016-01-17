package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(BusinessPartner.class)
public class BusinessPartner_ {

    public static volatile SingularAttribute<BusinessPartner, Integer> id;
    public static volatile SingularAttribute<BusinessPartner, String> activityCode;
    public static volatile SingularAttribute<BusinessPartner, Integer> interestFreeDays;
    public static volatile SingularAttribute<BusinessPartner, String> phone;
    public static volatile SingularAttribute<BusinessPartner, String> fax;
    public static volatile SingularAttribute<BusinessPartner, String> TIN;
    public static volatile SingularAttribute<BusinessPartner, String> EMail;
    public static volatile SingularAttribute<BusinessPartner, Long> version;
    public static volatile SingularAttribute<BusinessPartner, String> companyIdNumber;
    public static volatile SingularAttribute<BusinessPartner, String> currencyDesignation;
    public static volatile SingularAttribute<BusinessPartner, Address> address;
    public static volatile SingularAttribute<BusinessPartner, String> currentAccount;
    public static volatile SingularAttribute<BusinessPartner, String> name;
    public static volatile SingularAttribute<BusinessPartner, Integer> rebate;
    public static volatile SingularAttribute<BusinessPartner, Boolean> VAT;
    public static volatile SingularAttribute<BusinessPartner, String> name1;
    public static volatile SingularAttribute<BusinessPartner, ContactPerson> contactPerson;
    public static volatile SingularAttribute<BusinessPartner, BusinessPartner> parentBusinessPartner;
    public static volatile SingularAttribute<BusinessPartner, BusinessPartner.Type> type;
    public static volatile SingularAttribute<BusinessPartner, POSType> posType;
    public static volatile SingularAttribute<BusinessPartner, Integer> longitude;
    public static volatile SingularAttribute<BusinessPartner, Integer> latitude;
    public static volatile SingularAttribute<BusinessPartner, Integer> telekomId;
    public static volatile SingularAttribute<BusinessPartner, TelekomAddress> telekomAddress;
    public static volatile SingularAttribute<BusinessPartner, BusinessPartnerStatus> businessPartnerStatus;
    public static volatile SingularAttribute<BusinessPartner, BusinessPartner.TelekomStatus> telekomStatus;
    public static volatile SingularAttribute<BusinessPartner, String> remark;
    public static volatile SingularAttribute<BusinessPartner, BusinessPartnerStatus> status;

}