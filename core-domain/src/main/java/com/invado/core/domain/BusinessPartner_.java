package com.invado.core.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(BusinessPartner.class)
public class BusinessPartner_ { 

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

}